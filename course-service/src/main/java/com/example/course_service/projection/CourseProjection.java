package com.example.course_service.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CourseProjection {
    @Value("#{target.Id}")
    String getId();
    @Value("#{target.Name}")
    String getName();
    @Value("#{target.Price}")
    Double getPrice();
    @Value("#{target.CategoryName}")
    String getCategoryName();
}
