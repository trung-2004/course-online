package com.example.course_service.dto.response.question_item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionItemDTO {
    private String id;
    private String title;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answerCorrect;
    private Integer orderTop;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}