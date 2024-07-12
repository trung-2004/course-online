package com.example.course_service.service;

import com.example.course_service.dto.request.topic.CreateTopic;
import com.example.course_service.dto.request.topic.EditTopic;
import com.example.course_service.dto.response.course.CourseTopicDetailResponse;
import com.example.course_service.dto.response.topic.TopicDTO;

import java.util.List;

public interface TopicService {
    TopicDTO create(CreateTopic model);
    TopicDTO edit(EditTopic model);
    void delete(String[] ids);
    TopicDTO findById(String id);
    CourseTopicDetailResponse getDetailById(String id, String userId);

    List<String> getTopicIds(String courseId);
}
