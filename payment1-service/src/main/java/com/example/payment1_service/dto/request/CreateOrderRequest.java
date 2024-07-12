package com.example.payment1_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {
    private String orderId;
    private Integer status;
}
