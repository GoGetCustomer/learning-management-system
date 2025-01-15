package com.example.lms.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class QuizResponse {

    private final Long quizId;
    private final String quizTitle;
    private final LocalDateTime quizDueDate;
    private final List<QuestionResponse> questions;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionResponse {
        private final Long questionId;
        private final String content;
        private final String correct;
        private final Integer point;

    }
}
