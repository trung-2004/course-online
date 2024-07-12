package com.example.payment_service.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseUser {
    @NotBlank(message = "Course is mandatory")
    private String courseId;
//    @NotNull(message = "Price is mandatory")
//    @Positive(message = "Price must be greater than zero")
//    private Double price;
    @NotBlank(message = "Payment Method is mandatory")
    @Size(max = 1000, message = "Payment Method cannot exceed 1000 characters")
    private String paymentMethod;
    @NotBlank(message = "Bank Code is mandatory")
    @Size(max = 1000, message = "Bank Code cannot exceed 1000 characters")
    private String bankCode;
}
