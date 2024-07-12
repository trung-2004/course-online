package com.example.test_service.repository.httpclient;

import com.example.test_service.configuration.AuthenticationRequestInterceptor;
import com.example.test_service.dto.ApiResponse;
import com.example.test_service.dto.response.TopicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;

@FeignClient(name = "course-service", url = "${app.services.course}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface CourseClient {
    @GetMapping(path = "/topic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<TopicResponse> getTopic(@PathVariable("id") String id);
}
