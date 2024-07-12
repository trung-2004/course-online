package com.example.test_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test")
@Entity
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
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

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "topicId", nullable = false)
    private String topicId;
    @Column(name = "courseId", nullable = false)
    private String courseId;
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "pastMark", nullable = false)
    private Integer pastMark;
    @Column(name = "totalMark", nullable = false)
    private Integer totalMark;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "totalQuestion", nullable = false)
    private Integer totalQuestion;
    @Column(name = "time", nullable = false)
    private Integer time;
}
