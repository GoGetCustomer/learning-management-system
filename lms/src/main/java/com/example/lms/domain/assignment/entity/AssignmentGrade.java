package com.example.lms.domain.assignment.entity;

import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.student.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_grade_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "feedback", length = 300)
    private String feedback;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private AssignmentGrade(Assignment assignment, Student student, int grade, String feedback, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.assignment = assignment;
        this.student = student;
        this.grade = grade;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AssignmentGrade of(Assignment assignment, Student student, int grade, String feedback) {
        return AssignmentGrade.builder()
                .assignment(assignment)
                .student(student)
                .grade(grade)
                .feedback(feedback)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
