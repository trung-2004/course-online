package com.example.payment_service.dto.response;

import com.example.payment_service.entity.Status;

import java.sql.Timestamp;

public class PaymentResponse {
    private String id;
    private String orderId;
    private String userId;
    private Double amount;
    private String paymentMethod;
    private Status status;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
