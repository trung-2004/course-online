package com.example.test_service.repository;

import com.example.test_service.entity.AnswerStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerStudentRepository extends JpaRepository<AnswerStudent, String> {
}
