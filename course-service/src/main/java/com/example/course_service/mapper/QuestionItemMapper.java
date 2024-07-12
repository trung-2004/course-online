package com.example.course_service.mapper;

import com.example.course_service.dto.response.question_item.QuestionItemDTO;
import com.example.course_service.entity.QuestionItem;
import org.springframework.stereotype.Component;

@Component
public class QuestionItemMapper {
    public QuestionItemDTO toQuestionItemDTO(QuestionItem model){
        if (model == null) {
            return null;
        }
        QuestionItemDTO questionItemDTO = QuestionItemDTO.builder()
                .id(model.getId())
                .title(model.getTitle())
                .answer1(model.getAnswer1())
                .answer2(model.getAnswer2())
                .answer3(model.getAnswer3())
                .answer4(model.getAnswer4())
                .answerCorrect(model.getAnswerCorrect())
                .orderTop(model.getOrderTop())
                .createdBy(model.getCreatedBy())
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .build();
        return questionItemDTO;
    }
}
