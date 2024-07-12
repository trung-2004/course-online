package com.example.test_service.repository;

import com.example.test_service.entity.QuestionTest;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTestRepository extends JpaRepository<QuestionTest, String> {
    @Query(value = "SELECT * FROM question_test q WHERE q.topic_id = :topicId AND q.level = :level ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<QuestionTest> findRandomQuestionsByTopicIdAndLevel(@Param("topicId") String topicId, @Param("level") int level, @Param("limit") int limit);
}
