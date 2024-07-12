package com.example.course_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "image", nullable = false)
    private String image;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "star", nullable = false)
    private Double star;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "level", nullable = false)
    private Integer level;
    @Column(name = "language", nullable = false)
    private String language;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "trailer", nullable = false)
    private String trailer;
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

    @Column(name = "category_id")
    private String categoryId;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Topic> topics;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Certificate> certificates;
}
