package com.example.course_service.dto.request.course;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourse {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Image URL is mandatory")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL should be a valid URL")
    private String image;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Level is mandatory")
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 5, message = "Level must be at most 5")
    private Integer level;

    @NotBlank(message = "Language is mandatory")
    private String language;

    @NotBlank(message = "Trailer URL is mandatory")
    @Pattern(regexp = "^(http|https)://.*$", message = "Trailer URL should be a valid URL")
    private String trailer;

    @NotNull(message = "Category ID is mandatory")
    private String categoryId;
}
