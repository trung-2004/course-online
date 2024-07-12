package com.example.test_service.repository;

import com.example.test_service.entity.Test;
import com.example.test_service.entity.TestUser;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, String> {
    TestUser findByTestAndUserIdAndStatusAndStatusStudy(Test test, String userId, boolean bool, boolean bool1);
    List<TestUser> findAllByTestAndUserId(Test test, String userId);
    @Query("SELECT tu FROM TestUser tu WHERE tu.test = :test AND tu.userId = :userId AND tu.createdDate BETWEEN :startOfDay AND :endOfDay")
    List<TestUser> findAttemptsByUserAndDate(@Param("test") Test test, @Param("userId") String userId, @Param("startOfDay") Timestamp startOfDay, @Param("endOfDay") Timestamp endOfDay);
}
