package com.example.lms.domain.quiz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizRequest {

    private String quizTitle;
    // TODO : Course 엔티티와 연관관계로 대체
    private Long courseId;
    private LocalDateTime quizDueDate;
    private List<QuestionRequest> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuestionRequest {
        private String content;
        private String correct;
        private Integer point;
    }
}