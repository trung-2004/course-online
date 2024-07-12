package com.example.course_service.repository.httpclient;

import com.example.course_service.configuration.AuthenticationRequestInterceptor;
import com.example.course_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "payment-service", url = "http://localhost:8084/payment", configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @GetMapping("/get-by-user")
    ApiResponse<List<String>> getAllIdByUser();

    @GetMapping("/check-buy-course/{courseId}")
    ApiResponse<Boolean> checkBuyCourse(@PathVariable("courseId") String courseId);
}
