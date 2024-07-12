package com.example.course_service.repository.httpclient;

import com.example.course_service.configuration.AuthenticationRequestInterceptor;
import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.response.topic.TestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "test-service", url = "${app.services.test}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface TestClient {
    @GetMapping(value = "/by-topic/{topicId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<TestDTO> getTest(@PathVariable("topicId") String topicId);
    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> check(@RequestBody List<String> topicIds);
}
