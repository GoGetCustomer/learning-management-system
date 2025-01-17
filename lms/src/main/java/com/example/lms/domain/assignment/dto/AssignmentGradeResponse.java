package com.example.lms.domain.assignment.dto;

import com.example.lms.domain.assignment.entity.AssignmentGrade;
import lombok.Getter;

@Getter
public class AssignmentGradeResponse {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private int grade;
    private String feedback;

    public AssignmentGradeResponse(AssignmentGrade grade) {
        this.id = grade.getId();
        this.assignmentId = grade.getAssignment().getId();
        this.studentId = grade.getStudent().getId();
        this.grade = grade.getGrade();
        this.feedback = grade.getFeedback();
    }
}