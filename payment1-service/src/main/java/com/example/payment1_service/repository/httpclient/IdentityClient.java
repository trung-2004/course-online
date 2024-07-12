package com.example.payment1_service.repository.httpclient;

import com.example.payment1_service.dto.ApiResponse;
import com.example.payment1_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${app.services.identity}")
public interface IdentityClient {
    @GetMapping(value = "/any/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId);
}
