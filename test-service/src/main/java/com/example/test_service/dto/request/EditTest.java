package com.example.test_service.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditTest {
    private String id;
    private String title;
    private String topicId;
    private String description;
    private Integer pastMark;
    private Integer totalMark;
    private Integer time;
    private Integer type;
}
