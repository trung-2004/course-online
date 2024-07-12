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
@Table(name = "testUser")
@Entity
@Builder
public class TestUser {
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
    @Column(name = "score")
    private Double score;
    @Column(name = "time")
    private Integer time;
    @Column(name = "status")
    private boolean status;
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "statusStudy")
    private boolean statusStudy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testId")
    private Test test;
    @OneToMany(mappedBy = "testUser")
    @JsonIgnore
    private List<AnswerStudent> answerStudents;

    @OneToMany(mappedBy = "testUser")
    @JsonIgnore
    private List<QuestionTestUser> questionTestUsers;
}
