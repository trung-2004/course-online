package com.example.payment1_service.repository.httpclient;

import com.example.payment1_service.configuration.AuthenticationRequestInterceptor;
import com.example.payment1_service.dto.ApiResponse;
import com.example.payment1_service.dto.response.CourseUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service", url = "${app.services.payment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @GetMapping(path = "/any/course-user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CourseUserDTO> getCouseUser(@PathVariable("id") String id);
}
