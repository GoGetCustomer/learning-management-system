package com.example.lms.domain.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSubmissionResponse {
    private String status;
    private String message;
    private int totalScore;
    private int correctScore;
}
