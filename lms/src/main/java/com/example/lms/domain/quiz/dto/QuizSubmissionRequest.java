package com.example.lms.domain.quiz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class QuizSubmissionRequest {
    private Long studentId;
    private Map<Long, String> answers;

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }
}
