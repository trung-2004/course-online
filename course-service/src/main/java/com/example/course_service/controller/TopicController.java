package com.example.course_service.controller;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.topic.CreateTopic;
import com.example.course_service.dto.request.topic.EditTopic;
import com.example.course_service.dto.response.course.CourseTopicDetailResponse;
import com.example.course_service.dto.response.topic.TopicDTO;
import com.example.course_service.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/any/topic/{courseId}")
    ResponseEntity<ApiResponse<List<String>>> getTopicIdsByCourseId(@PathVariable("courseId") String courseId) {
        List<String> topicIDs = topicService.getTopicIds(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", topicIDs)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/topic/{id}")
    ResponseEntity<ApiResponse<TopicDTO>> getById(@PathVariable("id") String id) {
        TopicDTO topicDTO = topicService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", topicDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/topic/insert")
    ResponseEntity<ApiResponse<TopicDTO>> createTopic(@RequestBody @Valid CreateTopic model) {
        TopicDTO topicDTO = topicService.create(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", topicDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/topic/edit")
    ResponseEntity<ApiResponse<TopicDTO>> editTopic(@RequestBody @Valid EditTopic model) {
        TopicDTO topicDTO = topicService.edit(model);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", topicDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/topic/delete")
    ResponseEntity<ApiResponse<String>> delete(@RequestBody String[] ids) {
        topicService.delete(ids);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", "")
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/topic/detail/{id}")
    ResponseEntity<ApiResponse<CourseTopicDetailResponse>> getDetailById(@PathVariable("id") String id) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUser = jwt.getClaimAsString("userId");
        CourseTopicDetailResponse courseTopicDetailResponse = topicService.getDetailById(id, idUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseTopicDetailResponse)
        );
    }
}
