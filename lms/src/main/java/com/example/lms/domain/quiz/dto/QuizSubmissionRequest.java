package com.example.lms.domain.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuizSubmissionRequest {
    private Long studentId;
    private Map<Long, String> answers;
}
