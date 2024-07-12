package com.example.payment1_service.mapper;

import com.example.payment1_service.dto.response.PaymentDTO;
import com.example.payment1_service.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentDTO toPaymentDTO(Payment payment) {
        if (payment == null) return null;
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .createdBy(payment.getCreatedBy())
                .createdDate(payment.getCreatedDate())
                .modifiedBy(payment.getModifiedBy())
                .modifiedDate(payment.getModifiedDate())
                .build();
        return paymentDTO;
    }
}
