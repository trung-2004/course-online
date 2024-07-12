package com.example.test_service.entity;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answerStudent")
@Entity
@Builder
public class AnswerStudent {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionTestId")
    private QuestionTest questionTest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testUserId")
    private TestUser testUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answerId")
    private Answer answer;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "isCorrect")
    private boolean isCorrect;
}
