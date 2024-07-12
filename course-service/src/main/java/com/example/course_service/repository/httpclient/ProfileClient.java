package com.example.course_service.repository.httpclient;

import com.example.course_service.configuration.AuthenticationRequestInterceptor;
import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.response.UserProfileResponse;
import com.example.course_service.dto.response.topic.TestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/users-by-userId", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getProfile();
}
