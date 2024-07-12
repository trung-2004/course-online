package com.example.payment_service.repository;

import com.example.payment_service.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, String> {
    @Query("SELECT c.courseId FROM CourseUser c WHERE c.userId = :userId")
    List<String> findCourseIdsByUserId(@Param("userId") String userId);

    List<CourseUser> findAllByCourseIdAndUserIdAndStatus(String courseId, String userId, Integer status);

}
