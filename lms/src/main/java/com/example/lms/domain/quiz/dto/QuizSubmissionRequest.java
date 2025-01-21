package com.example.lms.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class QuizSubmissionRequest {
    @Schema(description = "학생 ID", example = "1")
    private Long studentId;

    @Schema(
            description = "퀴즈 답변 목록",
            example = "{\"1\": \"Answer1\", \"2\": \"Answer2\"}"
    )
    private Map<Long, String> answers;

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }
}
