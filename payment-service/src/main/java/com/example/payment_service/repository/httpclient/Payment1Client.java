package com.example.payment_service.repository.httpclient;

import com.example.payment_service.configuration.AuthenticationRequestInterceptor;
import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.request.PaymentRequest;
import com.example.payment_service.dto.response.CourseResponse;
import com.example.payment_service.dto.response.PaymentDTO1;
import com.example.payment_service.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment1-service", url = "${app.services.payment1}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface Payment1Client {
    @PostMapping(value = "/buy-course", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PaymentDTO1.VNPayResponse> creatPayment(@RequestBody PaymentRequest paymentRequest);
}
