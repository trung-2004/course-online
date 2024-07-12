package com.example.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courseUser")
@Entity
@Builder
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "courseId", nullable = false)
    private String courseId;
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "paymentMethod", nullable = false)
    private String paymentMethod;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "purchaseDate")
    private Date purchaseDate;
    @Column(name = "expiryDate")
    private Date expiryDate;
    @Column(name = "createddate")
    @CreatedDate
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    @LastModifiedDate
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    @CreatedBy
    private String createdBy;
    @Column(name = "modifiedby")
    @LastModifiedBy
    private String modifiedBy;
}
