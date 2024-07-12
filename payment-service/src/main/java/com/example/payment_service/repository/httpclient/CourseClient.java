package com.example.payment_service.repository.httpclient;

import com.example.payment_service.configuration.AuthenticationRequestInterceptor;
import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.response.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${app.services.course}")//,configuration = {AuthenticationRequestInterceptor.class}
public interface CourseClient {
    @GetMapping(value = "/any/course-online/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CourseResponse> getCourseById(@PathVariable("id") String id);
}
