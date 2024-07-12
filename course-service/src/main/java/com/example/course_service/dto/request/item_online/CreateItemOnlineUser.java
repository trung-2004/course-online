package com.example.course_service.dto.request.item_online;

import lombok.Data;

@Data
public class CreateItemOnlineUser {
    private String courseId;
    private String userId;
}
