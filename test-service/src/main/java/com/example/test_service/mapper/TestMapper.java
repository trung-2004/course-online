package com.example.test_service.mapper;

import com.example.test_service.dto.response.TestDTO;
import com.example.test_service.entity.Test;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    public TestDTO toTestDTO(Test model){
        if (model == null) return null;
        TestDTO testDTO = TestDTO.builder()
                .id(model.getId())
                .time(model.getTime())
                .title(model.getTitle())
                .type(model.getType())
                .totalQuestion(model.getTotalQuestion())
                .createdBy(model.getCreatedBy())
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .description(model.getDescription())
                .build();
        return testDTO;
    }
}
