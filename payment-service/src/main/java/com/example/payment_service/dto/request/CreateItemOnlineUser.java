package com.example.payment_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemOnlineUser {
    private String courseId;
    private String userId;
}
