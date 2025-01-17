package com.example.lms.domain.assignment.entity;

import com.example.lms.domain.course.entity.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    private String description;

    private LocalDate dueDate;

    @Builder
    private Assignment(Course course, String title, String description, LocalDate dueDate) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public static Assignment of(Course course, String title, String description, LocalDate dueDate) {
        return Assignment.builder()
                .course(course)
                .title(title)
                .description(description)
                .dueDate(dueDate)
                .build();
    }
}
