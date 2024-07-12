package com.example.course_service.service;

import com.example.course_service.dto.request.course.CreateCourse;
import com.example.course_service.dto.request.course.EditCourse;
import com.example.course_service.dto.response.course.CourseDTO;
import com.example.course_service.dto.response.course.CourseDTOTopicId;
import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.dto.response.course.CourseResponse;
import com.example.course_service.entity.Course;
import com.example.course_service.projection.CourseProjection;
import com.example.course_service.projection.CourseProjectionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourseService {
    Page<CourseDTO> findAll(Specification<Course> spec, Pageable pageable);
    List<CourseResponse> findAllByStudent(String studentId);
    CourseDTO create(CreateCourse model);
    CourseDTO edit(EditCourse model);
    void delete(String[] ids);
    CourseDTOTopicId findById(String id);
    CourseDetail getDetail(String id);
    List<CourseDTO> getListRelated(String id);
    List<CourseDTO> findCourseTop6();
    List<CourseProjection> findAllByProjection();
    List<CourseProjectionDTO> findAllByProjectionDTO();
    //CourseOnlineDetail getDetail(String slug);
}
