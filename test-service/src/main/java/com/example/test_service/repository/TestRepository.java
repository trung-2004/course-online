package com.example.test_service.repository;

import com.example.test_service.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, String> {
    Test findByTopicId(String topicId);
}
