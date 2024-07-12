package com.example.test_service.mapper;

import com.example.test_service.dto.response.AnswerDTO;
import com.example.test_service.entity.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public AnswerDTO toAnswerDTO(Answer answer){
        if (answer == null) return null;
        AnswerDTO answerDTO = AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionTestId(answer.getQuestionTest().getId())
                .modifiedDate(answer.getModifiedDate())
                .modifiedBy(answer.getModifiedBy())
                .createdBy(answer.getCreatedBy())
                .createdDate(answer.getCreatedDate())
                .build();
        return answerDTO;
    }
}
