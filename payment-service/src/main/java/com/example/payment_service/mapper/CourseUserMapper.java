package com.example.payment_service.mapper;

import com.example.payment_service.dto.response.CourseUserDTO;
import com.example.payment_service.entity.CourseUser;
import org.springframework.stereotype.Component;

@Component
public class CourseUserMapper {
    public CourseUserDTO toCourseUserDTO(CourseUser courseUser){
        if (courseUser == null) return null;
        CourseUserDTO courseUserDTO = CourseUserDTO.builder()
                .id(courseUser.getId())
                .courseId(courseUser.getCourseId())
                .userId(courseUser.getUserId())
                .price(courseUser.getPrice())
                .status(courseUser.getStatus())
                .paymentMethod(courseUser.getPaymentMethod())
                .createdBy(courseUser.getCreatedBy())
                .createdDate(courseUser.getCreatedDate())
                .modifiedBy(courseUser.getModifiedBy())
                .modifiedDate(courseUser.getModifiedDate())
                .build();
        return courseUserDTO;
    }
}
