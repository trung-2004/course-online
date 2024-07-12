package com.example.course_service.controller;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.certificate.CreateCertificate;
import com.example.course_service.dto.request.course.CreateCourse;
import com.example.course_service.dto.response.certificate.CertificateDTO;
import com.example.course_service.dto.response.course.CourseDTO;
import com.example.course_service.entity.Certificate;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.repository.CertificateRepository;
import com.example.course_service.service.CertificateService;
//import com.example.course_service.service.impl.PdfService;
import com.example.course_service.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CertificateController {
    private final CertificateService certificateService;
    private final CertificateRepository certificateRepository;
    private final JwtUtils jwtUtils;
    //private final PdfService pdfService;

    @PostMapping("/certificate/complete-course/{courseId}")
    ResponseEntity<ApiResponse<CertificateDTO>> createCertificate(@PathVariable("courseId") String courseId) {
        String userId = jwtUtils.getUserIdFromJwt();
        CreateCertificate certificate = new CreateCertificate();
        certificate.setCourseId(courseId);
        certificate.setUserId(userId);
        CertificateDTO certificateDTO = certificateService.create(certificate);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", certificateDTO)
        );
    }

//    @GetMapping("/any/generate-certificate/{id}")
//    public ResponseEntity<byte[]> generateCertificate(@PathVariable("id") String certificateId) throws IOException {
//        Certificate certificate = certificateRepository.findById(certificateId)
//                .orElseThrow(() -> new AppException(ErrorCode.CERTIFICATE_NOTFOUND));
//        byte[] pdfBytes = pdfService.generatePdf(certificate);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "certificate.pdf");
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(pdfBytes);
//    }
}
