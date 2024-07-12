package com.example.test_service.dto.response;

import com.example.test_service.entity.QuestionTest;
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
public class TestDetail {
    private String id;
    private String title;
    private Integer type;
    private String description;
    private Integer time;
    private Integer totalQuestion;
    private List<QuestionTestDTO> questionTestDTOS;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
