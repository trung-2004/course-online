package com.example.payment1_service.controller;

import com.example.payment1_service.dto.ApiResponse;
import com.example.payment1_service.dto.request.CreatePayment;
import com.example.payment1_service.dto.response.PaymentDTO;
import com.example.payment1_service.dto.response.PaymentDTO1;
import com.example.payment1_service.service.PaymentService;
import com.example.payment1_service.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @PostMapping("/buy-course")
    public ResponseEntity<ApiResponse<PaymentDTO1.VNPayResponse>> buyCourse(@RequestBody @Valid CreatePayment request){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        PaymentDTO paymentDTO = paymentService.buyCourse(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1001, "Success", paymentService.createVnPayPayment(request))
        );
    }



    @GetMapping("/any/return-vnpay")
    public ResponseEntity<ApiResponse<PaymentDTO1.VNPayResponse>> returnVNPAY(HttpServletRequest request){
        PaymentDTO1.VNPayResponse list = paymentService.returnVnPay(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", list)
        );
    }


}
