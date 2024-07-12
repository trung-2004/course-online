package com.example.course_service.service.impl;

import com.example.course_service.dto.ApiResponse;
import com.example.course_service.dto.request.certificate.CreateCertificate;
import com.example.course_service.dto.response.UserProfileResponse;
import com.example.course_service.dto.response.certificate.CertificateDTO;
import com.example.course_service.entity.Certificate;
import com.example.course_service.entity.Course;
import com.example.course_service.entity.Topic;
import com.example.course_service.exception.AppException;
import com.example.course_service.exception.ErrorCode;
import com.example.course_service.mapper.CertificateMapper;
import com.example.course_service.repository.CertificateRepository;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.TopicRepository;
import com.example.course_service.repository.httpclient.ProfileClient;
import com.example.course_service.repository.httpclient.TestClient;
import com.example.course_service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final TestClient testClient;
    private final ProfileClient profileClient;
    private final CertificateMapper certificateMapper;

    @Override
    public CertificateDTO create(CreateCertificate model) {
        Course course = courseRepository.findById(model.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        Certificate certificateExisting = certificateRepository.findByCourseAndUserId(course, model.getUserId());
        if (certificateExisting != null) {
            return certificateMapper.toCertificateDTO(certificateExisting);
        }
        List<String> topicIds = topicRepository.findTopicIdsByCourseId(course.getId());
        ApiResponse<Boolean> check = testClient.check(topicIds);
        if (!check.getResult()) throw new AppException(ErrorCode.CERTIFICATE_QUALIFIED);

        ApiResponse<UserProfileResponse> profileResponseApiResponse = profileClient.getProfile();
        if (profileResponseApiResponse == null) throw new AppException(ErrorCode.PROFILE_UPDATE);

        Certificate certificate = Certificate.builder()
                .course(course)
                .userId(model.getUserId())
                .issuedDate(new Timestamp(System.currentTimeMillis()))
                .downloadsCount(0)
                .fullName(profileResponseApiResponse.getResult().getFirstName() + " " + profileResponseApiResponse.getResult().getLastName())
                .createdBy(profileResponseApiResponse.getResult().getFirstName() + " " + profileResponseApiResponse.getResult().getLastName())
                .modifiedBy(profileResponseApiResponse.getResult().getFirstName() + " " + profileResponseApiResponse.getResult().getLastName())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                .build();
        certificateRepository.save(certificate);

        return certificateMapper.toCertificateDTO(certificate);
    }
    @Transactional
    public String demo(){
        return string1();
    }
    @Async
    public String string1() {
        return "Heelo";
    }
}
