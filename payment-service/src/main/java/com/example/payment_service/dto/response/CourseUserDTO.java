package com.example.payment_service.dto.response;

import com.example.payment_service.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CourseUserDTO {
    private String id;
    private String courseId;
    private String userId;
    private Double price;
    private Integer status;
    private String paymentMethod;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private String createdBy;
    private String modifiedBy;

}
