package com.example.course_service.repository;

import com.example.course_service.entity.Certificate;
import com.example.course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    Certificate findByCourseAndUserId(Course course, String userId);
}
