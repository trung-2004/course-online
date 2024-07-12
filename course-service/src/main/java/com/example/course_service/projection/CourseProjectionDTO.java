package com.example.course_service.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProjectionDTO {
    private String id;
    private String name;
    private Double price;
    private String categoryName;
}
