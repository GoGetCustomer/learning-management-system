package com.example.lms.domain.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignmentGradeRequest {
    private Long studentId;
    private int grade;
    private String feedback;
}