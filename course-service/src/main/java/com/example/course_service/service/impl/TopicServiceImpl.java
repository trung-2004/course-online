package com.example.course_service.service.impl;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.item_online.CreateItemOnlineUser;
import com.example.course_service.dto.request.topic.CreateTopic;
import com.example.course_service.dto.request.topic.EditTopic;
import com.example.course_service.dto.response.course.CourseTopicDetailResponse;
import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.dto.response.topic.TopicDetailResponse;
import com.example.course_service.entity.Course;
import com.example.course_service.entity.ItemOnline;
import com.example.course_service.entity.ItemOnlineUser;
import com.example.course_service.entity.Topic;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.mapper.TopicMapper;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.ItemOnlineUserRepository;
import com.example.course_service.repository.TopicRepository;
import com.example.course_service.repository.httpclient.PaymentClient;
import com.example.course_service.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final ItemOnlineUserRepository itemOnlineUserRepository;
    private final TopicMapper topicMapper;
    private final PaymentClient paymentClient;

    @Override
    public TopicDTO create(CreateTopic model) {
        Course course = courseRepository.findById(model.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        Topic topic = Topic.builder()
                .name(model.getName())
                .orderTop(model.getOrderTop())
                .course(course)
                .createdBy("Demo")
                .modifiedBy("Demo")
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                .build();
        return topicMapper.toTopicDTO(topicRepository.save(topic));
    }

    @Override
    public TopicDTO edit(EditTopic model) {
        Topic topic = topicRepository.findById(model.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTFOUND));
        Course course = courseRepository.findById(model.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        topic.setName(model.getName());
        topic.setOrderTop(model.getOrderTop());
        topic.setCourse(course);
        topic.setModifiedBy("Demo");
        topic.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        return topicMapper.toTopicDTO(topicRepository.save(topic));
    }

    @Override
    public void delete(String[] ids) {
        topicRepository.deleteAllById(List.of(ids));
    }

    @Override
    public TopicDTO findById(String id) {
        return topicMapper.toTopicDTO(topicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTFOUND)));
    }

    @Override
    public CourseTopicDetailResponse getDetailById(String id, String userId) {
        Course course = courseRepository.findByIdAndStatus(id, 1)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        // check buy course?
        ApiResponse<Boolean> checkBuy = paymentClient.checkBuyCourse(id);
        if (!checkBuy.getResult()) throw new AppException(ErrorCode.COURSE_NOTFOUND);

        List<Topic> topicList = topicRepository.findAllByCourse(course);
        List<TopicDetailResponse> topicDetailResponseList = new ArrayList<>();

        for (Topic topic: topicList) {
            TopicDetailResponse topicDetailResponse = topicMapper.toTopicOnlineAndStudentDetailResponse(topic, userId);
            topicDetailResponseList.add(topicDetailResponse);
        }
        topicDetailResponseList.sort(Comparator.comparingInt(TopicDetailResponse::getOrderTop));

        // process and status
        Long Totalduration = getTotalDurationCourse(course);
        Long Realduration = getRealDurationCourse(course, userId);

        double percentage = (double) Realduration / Totalduration * 100;
        // Làm tròn phần trăm thành một số nguyên
        int roundedPercentage = (int) Math.round(percentage);

        CourseTopicDetailResponse courseTopicDetailResponse = CourseTopicDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .topicOnlineDetailResponseList(topicDetailResponseList)
                .status(false)
                .progress(roundedPercentage)
                .build();

        return courseTopicDetailResponse;
    }

    @Override
    public List<String> getTopicIds(String courseId) {
        List<String> topicIds = topicRepository.findTopicIdsByCourseId(courseId);
        return topicIds;
    }

    @Transactional
    @KafkaListener(id = "topicItemOnlineGroup", topics = "topicItemOnline")
    public void listen(CreateItemOnlineUser itemOnlineUser) {
        try {
            log.info("Received: {}", itemOnlineUser.getCourseId());

            Course course = courseRepository.findById(itemOnlineUser.getCourseId())
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));

            for (Topic topic : course.getTopics()) {
                for (ItemOnline itemOnline : topic.getItemOnlines()) {
                    ItemOnlineUser itemOnlineUserExisting = itemOnlineUserRepository.findByItemOnlineAndUserId(itemOnline, itemOnlineUser.getUserId());
                    if (itemOnlineUserExisting == null){
                        ItemOnlineUser onlineUser = ItemOnlineUser.builder()
                                .itemOnline(itemOnline)
                                .userId(itemOnlineUser.getUserId())
                                .status(false)
                                .createdBy("Demo")
                                .modifiedBy("Demo")
                                .createdDate(new Timestamp(System.currentTimeMillis()))
                                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                                .build();
                        itemOnlineUserRepository.save(onlineUser);
                    } else {
                        itemOnlineUserExisting.setStatus(false);
                        itemOnlineUserExisting.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                        itemOnlineUserRepository.save(itemOnlineUserExisting);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing itemOnlineUser: {}", itemOnlineUser, e);
            // Optionally, you can rethrow the exception or handle it accordingly
            // throw e;
        }
    }

    private Long getTotalDurationCourse(Course course) {
        Long duration = 0L;
        for (Topic topic: course.getTopics()) {
            for (ItemOnline itemOnline: topic.getItemOnlines()) {
                duration += itemOnline.getDuration();
            }
        }
        return duration;
    }
    private Long getRealDurationCourse(Course course, String userId) {
        Long duration = 0L;
        for (Topic topic: course.getTopics()) {
            for (ItemOnline itemOnline: topic.getItemOnlines()) {
                ItemOnlineUser itemOnlineUser = itemOnlineUserRepository.findByItemOnlineAndUserId(itemOnline, userId);
                if (itemOnlineUser.isStatus()){
                    duration+= itemOnline.getDuration();
                }
                else {

                }
            }
        }
        return duration;
    }
}
