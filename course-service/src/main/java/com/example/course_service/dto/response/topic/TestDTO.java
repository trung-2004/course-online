package com.example.course_service.dto.response.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestDTO {
    private String id;
    private String title;
    private Integer type;
    private String description;
    private Integer time;
    private boolean status;
    private Integer totalQuestion;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
