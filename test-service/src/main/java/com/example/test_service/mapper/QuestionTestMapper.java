package com.example.test_service.mapper;

import com.example.test_service.dto.response.AnswerDTO;
import com.example.test_service.dto.response.QuestionTestDTO;
import com.example.test_service.entity.QuestionTest;
import com.example.test_service.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionTestMapper {
    private final AnswerMapper answerMapper;
    public QuestionTestDTO toQuestionTestDTO(QuestionTest questionTest){
        if (questionTest == null) return null;
        List<AnswerDTO> answerDTOS = questionTest.getAnswers().stream().map(answerMapper::toAnswerDTO).collect(Collectors.toList());
        QuestionTestDTO questionTestDTO = QuestionTestDTO.builder()
                .id(questionTest.getId())
                .title(questionTest.getTitle())
                .audiomp3(questionTest.getAudiomp3())
                .image(questionTest.getImage())
                .paragraph(questionTest.getParagraph())
                .type(questionTest.getType())
                .level(questionTest.getLevel())
                .answerDTOS(answerDTOS)
                .orderTop(questionTest.getOrderTop())
                .createdBy(questionTest.getCreatedBy())
                .modifiedBy(questionTest.getModifiedBy())
                .createdDate(questionTest.getCreatedDate())
                .modifiedDate(questionTest.getModifiedDate())
                .build();

        return questionTestDTO;
    }
}
