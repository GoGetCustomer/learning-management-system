package com.example.lms.domain.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizResponse {

    private Long quizId;
    private String quizTitle;
    private LocalDateTime quizDueDate;
    private List<QuestionResponse> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuestionResponse {
        private Long questionId;
        private String content;
        private String correct;
        private Integer point;

        public QuestionResponse(Long questionId, String content, String correct, Integer point) {
            this.questionId = questionId;
            this.content = content;
            this.correct = correct;
            this.point = point;
        }
    }

    public QuizResponse(Long quizId, String quizTitle, LocalDateTime quizDueDate, List<QuestionResponse> questions) {
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.quizDueDate = quizDueDate;
        this.questions = questions;
    }
}