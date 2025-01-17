package com.example.lms.domain.assignment.entity;

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
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String fileUrl;

    private LocalDateTime submittedAt;

    @Builder
    private Submission(Assignment assignment, Student student, String fileUrl, LocalDateTime submittedAt) {
        this.assignment = assignment;
        this.student = student;
        this.fileUrl = fileUrl;
        this.submittedAt = submittedAt;
    }

    public static Submission of(Assignment assignment, Student student, String fileUrl, LocalDateTime submittedAt) {
        return Submission.builder()
                .assignment(assignment)
                .student(student)
                .fileUrl(fileUrl)
                .submittedAt(submittedAt)
                .build();
    }
}