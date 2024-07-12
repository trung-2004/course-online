package com.example.course_service.dto.response.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {
    private String id;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private String userId;
    private String courseId;
    private String courseName;
    private String fullName;
    private Timestamp issuedDate;
}
