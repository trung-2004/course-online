package com.example.course_service.dto.response.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String id;
    private String name;
    private String image;
    private Double price;
    private Double star;
    private String description;
    private Integer level;
    private String language;
    private Integer status;
    private String trailer;
    private String category_id;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
