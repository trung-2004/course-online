package com.example.course_service.schedule;

import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.entity.Course;
import com.example.course_service.entity.Topic;
import com.example.course_service.mapper.TopicMapper;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.ProcessRepository;
import com.example.course_service.repository.TopicRepository;
import com.example.course_service.service.impl.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class CacheCourse {

    public static String keyCourseDetails = "course_details:";
    public static String keyCourserelated = "related_product:";
    public static String keyQueueCourse = "course_cache_key";
    public static String keyQueueCourseRelated = "course_related_cache_key";
    public static String nameProcess = "course_detail";
    static int size = 10;
    private final RedisCacheService redisCacheService;
    private final CourseRepository courseRepository;
    private final ProcessRepository processRepository;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Scheduled(cron = "0 04 11 * * *")
    public void cacheCourseAll() {
        log.info("Start Cache full...");
        //multipthreadSaveAllCourse();
        int offset = 0;
        while (true){
            List<Course> list = courseRepository.findCoursesWithPagination(size, offset);
            if (list == null || list.isEmpty()){
                break;
            }
            List<CourseDetail> courseDetails = new ArrayList<>();
            for (Course course : list){
                List<Topic> topicList = topicRepository.findAllByCourse(course);
                List<TopicDTO> topicDTOList = new ArrayList<>();
                for (Topic topic : topicList){
                    TopicDTO topicDTO = topicMapper.toTopicDTO(topic);
                    topicDTOList.add(topicDTO);
                }
                CourseDetail courseDetail = CourseDetail.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .price(course.getPrice())
                        .status(course.getStatus())
                        .image(course.getImage())
                        .language(course.getLanguage())
                        .level(course.getLevel())
                        .duration("12")
                        .star(4.5)
                        .categoryId(course.getCategoryId())
                        .trailer(course.getTrailer())
                        .topicDetailList(topicDTOList)
                        .description(course.getDescription())
                        .createdBy(course.getCreatedBy())
                        .createdDate(course.getCreatedDate())
                        .modifiedBy(course.getModifiedBy())
                        .modifiedDate(course.getModifiedDate())
                        .build();
                courseDetails.add(courseDetail);
            }
            redisCacheService.lPushAllCourse(keyQueueCourse, courseDetails);
            redisCacheService.lPushAllCourse(keyQueueCourseRelated, courseDetails);
            offset+=size;
        }
        log.info("Cache finished.");
    }

    //@Scheduled(fixedRate = 30000)
    public void cacheCheckUpdate(){
        log.info("Start check course update");
        // get lastUpdatedAt of Process
        var lastUpdatedAt = processRepository.getLastUpdatedAt(nameProcess);
        // get lastModifiedDate of Course
        Timestamp lastModifiedDate = courseRepository.findMaxUpdatedAt();
        if (checkCourseUpdate(lastUpdatedAt, lastModifiedDate)){
            List<Course> list = courseRepository.findAllCourseUpdated(lastUpdatedAt);
            for (Course course : list){
                List<Topic> topicList = topicRepository.findAllByCourse(course);
                List<TopicDTO> topicDTOList = new ArrayList<>();
                for (Topic topic : topicList){
                    TopicDTO topicDTO = topicMapper.toTopicDTO(topic);
                    topicDTOList.add(topicDTO);
                }
                CourseDetail courseDetail = CourseDetail.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .price(course.getPrice())
                        .status(course.getStatus())
                        .image(course.getImage())
                        .language(course.getLanguage())
                        .level(course.getLevel())
                        .duration("12")
                        .categoryId(course.getCategoryId())
                        .star(4.5)
                        .trailer(course.getTrailer())
                        .topicDetailList(topicDTOList)
                        .description(course.getDescription())
                        .createdBy(course.getCreatedBy())
                        .createdDate(course.getCreatedDate())
                        .modifiedBy(course.getModifiedBy())
                        .modifiedDate(course.getModifiedDate())
                        .build();

                // save cache
                redisCacheService.setValue(keyCourseDetails+course.getId(), course);
            }
            processRepository.updateLastUpdatedAt(new Timestamp(System.currentTimeMillis()), nameProcess);
            log.info("Update course details on Redis cache successfully! " + keyCourseDetails);
        }
        log.info("There are no updated products");
    }

    private boolean checkCourseUpdate(Timestamp lastUpdatedAt, Timestamp lastModifiedDate){
        if (lastUpdatedAt == null || lastModifiedDate == null){
            return false;
        }
        if (lastModifiedDate.after(lastUpdatedAt)){
            return true;
        } else {
            return false;
        }
    }

//    // multipthread
//    private void multipthreadSaveAllCourse() {
//        executorService.submit(new ThreadCourse(redisCacheService));
//        executorService.submit(new ThreadCourse(redisCacheService));
//    }
}
