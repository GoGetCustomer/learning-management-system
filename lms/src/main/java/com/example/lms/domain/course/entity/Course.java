package com.example.lms.domain.course.entity;

import com.example.lms.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="course")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_title", length = 50, nullable = false)
    private String courseTitle;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

    @Column(name = "course_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "course_end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "course_capacity", nullable = false)
    private int courseCapacity;
}
