package com.example.test_service.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TopicResponse {
    private String id;
    private String name;
    private Integer orderTop;
    private String courseId;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
