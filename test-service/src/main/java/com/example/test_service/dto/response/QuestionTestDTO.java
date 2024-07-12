package com.example.test_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionTestDTO {
    private String id;
    private String title;
    private String audiomp3;
    private String image;
    private String paragraph;
    private Integer type;
    private Integer level;
    private Integer orderTop;
    private List<AnswerDTO> answerDTOS;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
