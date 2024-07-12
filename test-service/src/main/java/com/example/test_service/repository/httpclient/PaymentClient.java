package com.example.test_service.repository.httpclient;

import com.example.test_service.configuration.AuthenticationRequestInterceptor;
import com.example.test_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service", url = "${app.services.payment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @GetMapping(path = "/check-buy-course/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> checkBuyCourse(@PathVariable("courseId") String courseId);
}
