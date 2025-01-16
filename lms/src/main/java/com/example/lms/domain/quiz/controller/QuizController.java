package com.example.lms.domain.quiz.controller;

import com.example.lms.domain.quiz.dto.QuizRequest;
import com.example.lms.domain.quiz.dto.QuizResponse;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest quizRequest) {
        QuizResponse response = quizService.createQuiz(quizRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable Long quizId) {
        QuizResponse response = quizService.getQuiz(quizId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<QuizResponse>> getQuizzesByCourseId(@PathVariable Long courseId) {
        List<QuizResponse> response = quizService.getQuizzesByCourseId(courseId);
        return ResponseEntity.ok(response);
    }
}