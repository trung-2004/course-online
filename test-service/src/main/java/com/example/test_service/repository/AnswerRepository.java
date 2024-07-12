package com.example.test_service.repository;

import com.example.test_service.entity.Answer;
import com.example.test_service.entity.QuestionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
    Answer findByQuestionTestAndStatus(QuestionTest questionTest, boolean bool);
}
