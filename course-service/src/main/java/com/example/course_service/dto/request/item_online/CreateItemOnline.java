package com.example.course_service.dto.request.item_online;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemOnline {
    @NotBlank(message = "Name is mandatory")
    private String title;
    @NotNull(message = "Duration is mandatory")
    private String content;
    @NotNull(message = "Level is mandatory")
    @Min(value = 0, message = "Level must be at least 0")
    @Max(value = 3, message = "Level must be at most 3")
    private Integer itemType;
    @NotNull(message = "Duration is mandatory")
    private Integer duration;
    @NotNull(message = "Order Top is mandatory")
    private Integer orderTop;
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL should be a valid URL")
    private String pathUrl;
    @NotBlank(message = "Topic Id is mandatory")
    private String topicId;
}
