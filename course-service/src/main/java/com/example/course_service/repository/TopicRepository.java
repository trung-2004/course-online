package com.example.course_service.repository;

import com.example.course_service.entity.Course;
import com.example.course_service.entity.Topic;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {
    List<Topic> findAllByCourse(Course course);
    @Query("SELECT t.id FROM Topic t WHERE t.course.id = :courseId")
    List<String> findTopicIdsByCourseId(@Param("courseId") String courseId);
}
