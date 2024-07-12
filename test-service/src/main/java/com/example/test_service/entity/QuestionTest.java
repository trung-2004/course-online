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
@Table(name = "questionTest")
@Entity
@Builder
public class QuestionTest {
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
    @Column(name = "audiomp3")
    private String audiomp3;
    @Column(name = "image")
    private String image;
    @Column(name = "paragraph")
    private String paragraph;
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "level", nullable = false)
    private Integer level;
    @Column(name = "orderTop", nullable = false)
    private Integer orderTop;
    @Column(name = "topicId", nullable = false)
    private String topicId;

    @OneToMany(mappedBy = "questionTest")
    @JsonIgnore
    private List<Answer> answers;

    @OneToMany(mappedBy = "questionTest")
    @JsonIgnore
    private List<AnswerStudent> answerStudents;

    @OneToMany(mappedBy = "questionTest")
    @JsonIgnore
    private List<QuestionTestUser> questionTestUsers;
}
