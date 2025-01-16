package com.example.lms.domain.teaching.entity;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.student.entity.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "teaching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teaching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teaching_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
