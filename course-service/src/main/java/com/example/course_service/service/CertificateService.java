package com.example.course_service.service;

import com.example.course_service.dto.request.certificate.CreateCertificate;
import com.example.course_service.dto.response.certificate.CertificateDTO;

public interface CertificateService {

    CertificateDTO create(CreateCertificate model);
}
