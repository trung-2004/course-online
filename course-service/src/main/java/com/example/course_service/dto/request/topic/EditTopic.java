package com.example.course_service.dto.request.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditTopic {
    @NotNull(message = "Id is mandatory")
    private String id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Level is mandatory")
    private Integer orderTop;
    @NotNull(message = "Course ID is mandatory")
    private String courseId;
}
