package com.example.lms.domain.quiz.controller;

import com.example.lms.domain.quiz.dto.QuizSubmissionRequest;
import com.example.lms.domain.quiz.dto.QuizSubmissionResponse;
import com.example.lms.domain.quiz.service.QuizSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizSubmissionController {

    private final QuizSubmissionService quizSubmissionService;

    @PostMapping("/{quizId}/submissions")
    public ResponseEntity<QuizSubmissionResponse> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionRequest request
    ) {
        QuizSubmissionResponse response = quizSubmissionService.submitQuiz(quizId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
