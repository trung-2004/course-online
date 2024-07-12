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
public class CourseResponse {
    private String id;
    private String name;
    private String image;
    private Double price;
    private Integer level;
    private String language;
    //private boolean status;
   // private Integer progress;
    private Timestamp createdDate;
}
