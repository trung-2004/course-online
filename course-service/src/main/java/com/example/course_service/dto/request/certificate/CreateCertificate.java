package com.example.course_service.dto.request.certificate;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateCertificate {
    private String userId;
    private String courseId;
}
