package com.example.course_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questionItem")
@Builder
public class QuestionItem {
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
    @Column(name = "answer1", nullable = false)
    private String answer1;
    @Column(name = "answer2", nullable = false)
    private String answer2;
    @Column(name = "answer3", nullable = false)
    private String answer3;
    @Column(name = "answer4", nullable = false)
    private String answer4;
    @Column(name = "answerCorrect", nullable = false)
    private String answerCorrect;
    @Column(name = "orderTop", nullable = false)
    private Integer orderTop;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemOnline_id")
    private ItemOnline itemOnline;
}
