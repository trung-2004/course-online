package com.example.payment1_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
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
    private String categoryName;
    private List<String> topicIds;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
