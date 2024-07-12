package com.example.test_service.repository;

import com.example.test_service.entity.QuestionTestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTestUserRepository extends JpaRepository<QuestionTestUser, String> {

}
