package com.example.course_service.mapper;

import com.example.course_service.dto.response.course.CourseDTO;
import com.example.course_service.dto.response.course.CourseDTOTopicId;
import com.example.course_service.dto.response.course.CourseResponse;
import com.example.course_service.entity.Course;
import com.example.course_service.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    private final TopicRepository topicRepository;
    public  CourseDTO toCourseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setImage(course.getImage());
        dto.setPrice(course.getPrice());
        dto.setStar(course.getStar());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setLanguage(course.getLanguage());
        dto.setStatus(course.getStatus());
        dto.setTrailer(course.getTrailer());
        dto.setCategory_id(course.getCategoryId());
        dto.setCreatedDate(course.getCreatedDate());
        dto.setModifiedDate(course.getModifiedDate());
        dto.setCreatedBy(course.getCreatedBy());
        dto.setModifiedBy(course.getModifiedBy());


        return dto;
    }

    public CourseResponse toCourseResponse(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setName(course.getName());
        response.setImage(course.getImage());
        response.setPrice(course.getPrice());
        response.setLevel(course.getLevel());
        response.setLanguage(course.getLanguage());
        //response.setStatus(course.getStatus() == 1); // Assuming status is 1 for true and 0 for false
        response.setCreatedDate(course.getCreatedDate());

        // Assuming progress is calculated or fetched from elsewhere, setting it as null for now
        //response.setProgress(null); // Or fetch the progress from the appropriate source

        return response;
    }

    public CourseDTOTopicId toCourseDTOTopicId(Course course) {
        if (course == null) return null;
        CourseDTOTopicId dto = new CourseDTOTopicId();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setImage(course.getImage());
        dto.setPrice(course.getPrice());
        dto.setStar(course.getStar());
        dto.setDescription(course.getDescription());
        dto.setLevel(course.getLevel());
        dto.setLanguage(course.getLanguage());
        dto.setStatus(course.getStatus());
        dto.setTrailer(course.getTrailer());
        dto.setCategory_id(course.getCategoryId());
        dto.setCreatedDate(course.getCreatedDate());
        dto.setModifiedDate(course.getModifiedDate());
        dto.setCreatedBy(course.getCreatedBy());
        dto.setModifiedBy(course.getModifiedBy());
        dto.setTopicIds(topicRepository.findTopicIdsByCourseId(course.getId()));
        return dto;
    }
}
