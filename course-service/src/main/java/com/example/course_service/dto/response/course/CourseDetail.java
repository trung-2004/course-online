package com.example.course_service.dto.response.course;

import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.dto.response.topic.TopicDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetail {
    private String id;
    private String name;
    private String image;
    private Double price;
    private String description;
    private Integer level;
    private String language;
    private Integer status;
    private Double star;
    private String duration;
    private String trailer;
    private String categoryId;
    private List<TopicDTO> topicDetailList;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
