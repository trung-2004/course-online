package com.example.payment1_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemOnlineUser {
    private String courseId;
    private String userId;
}
