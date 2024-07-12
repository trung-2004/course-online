package com.example.course_service.dto.response.course;


import com.example.course_service.dto.response.topic.TopicDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTopicDetailResponse {
    private String id;
    private String name;
    private boolean status;
    private Integer progress;
    private List<TopicDetailResponse> topicOnlineDetailResponseList;
}
