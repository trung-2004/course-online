package com.example.test_service.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SubmitTest {
    private int time;
    private List<CreateAnswerStudent> createAnswerStudents;
}
