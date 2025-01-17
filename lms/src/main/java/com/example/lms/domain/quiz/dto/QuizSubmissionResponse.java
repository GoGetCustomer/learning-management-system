package com.example.lms.domain.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSubmissionResponse {
    private String status;
    private String message;

    public QuizSubmissionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
