package com.example.payment1_service.service;

import com.example.payment1_service.dto.request.CreatePayment;
import com.example.payment1_service.dto.response.PaymentDTO;
import com.example.payment1_service.dto.response.PaymentDTO1;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentDTO buyCourse(String id, CreatePayment request);

    PaymentDTO1.VNPayResponse returnVnPay(HttpServletRequest request);
}
