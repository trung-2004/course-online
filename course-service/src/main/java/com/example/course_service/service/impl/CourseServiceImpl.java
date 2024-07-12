package com.example.course_service.service.impl;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.course.CreateCourse;
import com.example.course_service.dto.request.course.EditCourse;
import com.example.course_service.dto.response.course.CourseDTO;
import com.example.course_service.dto.response.course.CourseDTOTopicId;
import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.dto.response.course.CourseResponse;
import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.entity.Category;
import com.example.course_service.entity.Course;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.mapper.CourseMapper;
import com.example.course_service.mapper.TopicMapper;
import com.example.course_service.projection.CourseProjection;
import com.example.course_service.projection.CourseProjectionDTO;
import com.example.course_service.repository.CategoryRepository;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.httpclient.PaymentClient;
import com.example.course_service.schedule.CacheCourse;
import com.example.course_service.service.CourseService;
import com.example.course_service.util.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.course_service.schedule.CacheCourse.*;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;
    private final TopicMapper topicMapper;
    private final PaymentClient paymentClient;
    private final CacheCourse cacheCourse;
    private final RedisCacheService redisCacheService;

    @Override
    public Page<CourseDTO> findAll(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable).map(courseMapper::toCourseDTO);
    }

    @Override
    public List<CourseResponse> findAllByStudent(String studentId) {
        ApiResponse<List<String>> ids = paymentClient.getAllIdByUser();
        return courseRepository.findAllByIdIn(ids.getResult()).stream().map(courseMapper::toCourseResponse).collect(Collectors.toList());
    }

    @Override
    public CourseDTOTopicId findById(String id) {
        return courseMapper.toCourseDTOTopicId(courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND)));
    }

    @Override
    public CourseDetail getDetail(String id) {

        String redisKey = keyCourseDetails + id;
        // check cache
        CourseDetail course;
        course = redisCacheService.getValue(redisKey, CourseDetail.class);
        if (course == null) {

            Course course1 = courseRepository.findByIdAndStatus(id, 1)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
            List<TopicDTO> topicDTOList = course1.getTopics().stream().map(topicMapper::toTopicDTO).collect(Collectors.toList());
            course = CourseDetail.builder()
                    .id(course1.getId())
                    .name(course1.getName())
                    .price(course1.getPrice())
                    .status(course1.getStatus())
                    .image(course1.getImage())
                    .language(course1.getLanguage())
                    .level(course1.getLevel())
                    .duration("12")
                    .star(4.5)
                    .categoryId(course1.getCategoryId())
                    .trailer(course1.getTrailer())
                    .topicDetailList(topicDTOList)
                    .description(course1.getDescription())
                    .createdBy(course1.getCreatedBy())
                    .createdDate(course1.getCreatedDate())
                    .modifiedBy(course1.getModifiedBy())
                    .modifiedDate(course1.getModifiedDate())
                    .build();

            redisCacheService.setValue(redisKey, course);
            return course;
        }

        return course;
    }

    @Override
    public List<CourseDTO> getListRelated(String id) {
        String redisKeyRelated = keyCourserelated + id;
        String redisKey = keyCourseDetails;
        // check cache
        String courseIdRelated;
        courseIdRelated = redisCacheService.getValue(redisKeyRelated, String.class);
        List<CourseDTO> courseDTOList = new ArrayList<>();
        if (courseIdRelated != null) {
            courseIdRelated = courseIdRelated.replaceAll("\\[|\\]|\\s", "");
            // Chuyển đổi chuỗi thành danh sách
            List<String> list = Arrays.asList(courseIdRelated.split(","));
            for (String str : list) {
                CourseDetail courseDetail = redisCacheService.getValue(redisKey+str, CourseDetail.class);
                if (courseDetail != null) {
                    CourseDTO courseDTO = CourseDTO.builder()
                            .id(courseDetail.getId())
                            .name(courseDetail.getName())
                            .price(courseDetail.getPrice())
                            .status(courseDetail.getStatus())
                            .image(courseDetail.getImage())
                            .language(courseDetail.getLanguage())
                            .level(courseDetail.getLevel())
                            .language(courseDetail.getLanguage())
                            .description(courseDetail.getDescription())
                            .createdBy(courseDetail.getCreatedBy())
                            .createdDate(courseDetail.getCreatedDate())
                            .modifiedBy(courseDetail.getModifiedBy())
                            .modifiedDate(courseDetail.getModifiedDate())
                            .star(courseDetail.getStar())
                            .trailer(courseDetail.getTrailer())
                            .category_id(courseDetail.getCategoryId())
                            .build();
                    courseDTOList.add(courseDTO);
                } else {
                    Course course = courseRepository.findById(str).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
                    courseDTOList.add(courseMapper.toCourseDTO(course));
                }
            }
            return courseDTOList;
        }
        Course course = courseRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.COURSE_NOTFOUND));
        return courseRepository.findProductsByCategoryId(course.getCategoryId())
                .stream().map(courseMapper::toCourseDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CourseDTO> findCourseTop6() {
        List<CourseDTO> courses = courseRepository.getCourseTop6(6).stream().map(courseMapper::toCourseDTO).collect(Collectors.toList());
        return courses;
    }

    @Override
    public List<CourseProjection> findAllByProjection() {
        return courseRepository.getCourseAndCategoryNameProjection();
    }

    @Override
    public List<CourseProjectionDTO> findAllByProjectionDTO() {
        return courseRepository.getCourseAndCategoryNameProjectionDTO();
    }

    @Override
    public CourseDTO create(CreateCourse model) {
        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        Course course = Course.builder()
                .name(model.getName())
                .image(model.getImage())
                .price(model.getPrice())
                .description(model.getDescription())
                .level(model.getLevel())
                .language(model.getLanguage())
                .status(0)
                .star(0.0)
                .trailer(model.getTrailer())
                .categoryId(model.getCategoryId())
                .createdBy("Demo")
                .modifiedBy("Demo")
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                .build();
        courseRepository.save(course);

        return courseMapper.toCourseDTO(course);
    }

    @Override
    public CourseDTO edit(EditCourse model) {
        Course course = courseRepository.findById(model.getId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));

        Category category = categoryRepository.findById(model.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));

        course.setName(model.getName());
        course.setImage(model.getImage());
        course.setPrice(model.getPrice());
        course.setDescription(model.getDescription());
        course.setLevel(model.getLevel());
        course.setStatus(model.getStatus());
        course.setLanguage(model.getLanguage());
        course.setTrailer(model.getTrailer());
        course.setCategoryId(model.getCategoryId());
        course.setModifiedBy("Demo");
        course.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        courseRepository.save(course);

        if (course.getStatus() == 1){
            List<TopicDTO> topicDTOList = course.getTopics().stream().map(topicMapper::toTopicDTO).collect(Collectors.toList());
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

            redisCacheService.lPushAllCourse(keyQueueCourse, Collections.singletonList(courseDetail));
            redisCacheService.lPushAllCourse(keyQueueCourseRelated, Collections.singletonList(courseDetail));

            return courseMapper.toCourseDTO(course);
        }
        return courseMapper.toCourseDTO(course);
    }


    @Override
    public void delete(String[] ids) {
        for (String id: ids) {
            Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
            course.setStatus(0);
            courseRepository.save(course);

            String redisKey = keyCourseDetails + id;
            String redisKeyRelated = keyCourserelated + id;
            // check cache
            CourseDetail course1 = redisCacheService.getValue(redisKey, CourseDetail.class);
            String course2 = redisCacheService.getValue(redisKeyRelated, String.class);
            if (course1 != null) {
                redisCacheService.deleteValue(redisKey);
            }
            if (course2 != null) {
                redisCacheService.deleteValue(redisKeyRelated);
            }
        }
        //courseRepository.deleteAllById(List.of(ids));
    }
}
