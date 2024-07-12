package com.example.payment1_service.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class CreatePayment {
    private String orderId;
    private String paymentMethod;
    private String bankCode;
    private Double amount;
}
