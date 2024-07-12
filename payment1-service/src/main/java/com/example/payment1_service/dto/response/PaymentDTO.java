package com.example.payment1_service.dto.response;

import com.example.payment1_service.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PaymentDTO {
    private String id;
    private String orderId;
    private String userId;
    private Double amount;
    private String paymentMethod;
    private Integer status;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
