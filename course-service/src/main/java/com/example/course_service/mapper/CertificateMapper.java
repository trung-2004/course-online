package com.example.course_service.mapper;

import com.example.course_service.dto.response.certificate.CertificateDTO;
import com.example.course_service.entity.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {
    public CertificateDTO toCertificateDTO(Certificate certificate){
        if (certificate == null) return null;
        CertificateDTO certificateDTO = CertificateDTO.builder()
                .id(certificate.getId())
                .courseId(certificate.getCourse().getId())
                .courseName(certificate.getCourse().getName())
                .fullName(certificate.getFullName())
                .userId(certificate.getUserId())
                .issuedDate(certificate.getIssuedDate())
                .createdBy(certificate.getCreatedBy())
                .modifiedBy(certificate.getModifiedBy())
                .createdDate(certificate.getCreatedDate())
                .modifiedDate(certificate.getModifiedDate())
                .build();
        return certificateDTO;
    }
}
