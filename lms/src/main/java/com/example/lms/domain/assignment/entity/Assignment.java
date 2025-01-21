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
    @Column(name = "assignment_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "assignment_title", nullable = false, length = 50)
    private String title;

    @Column(name = "assignment_description", nullable = false, length = 200)
    private String description;

    @Column(name = "assignment_due_date", nullable = false)
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
