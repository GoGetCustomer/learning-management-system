package com.example.lms.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "퀴즈 제목", example = "수도 퀴즈")
    private String quizTitle;

    @Schema(description = "과정 ID", example = "1")
    private Long courseId;

    @Schema(description = "퀴즈 마감일", example = "2025-01-24T18:00:00")
    private LocalDateTime quizDueDate;

    @Schema(description = "퀴즈 질문 목록")
    private List<QuestionRequest> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuestionRequest {

        @Schema(description = "질문 내용", example = "대한민국의 수도는 어디인가요?")
        private String content;

        @Schema(description = "정답", example = "서울")
        private String correct;

        @Schema(description = "배점", example = "10")
        private Integer point;
    }
}