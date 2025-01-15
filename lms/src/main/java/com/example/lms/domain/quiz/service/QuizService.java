package com.example.lms.domain.quiz.service;

import com.example.lms.domain.quiz.dto.QuizRequest;
import com.example.lms.domain.quiz.dto.QuizResponse;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public QuizResponse createQuiz(QuizRequest quizRequest) {
        Quiz quiz = Quiz.createQuiz(
                quizRequest.getCourseId(),
                quizRequest.getQuizTitle(),
                quizRequest.getQuizDueDate()
        );

        Quiz savedQuiz = quizRepository.save(quiz);

        List<Question> questions = quizRequest.getQuestions().stream()
                .map(qr -> Question.createQuestion(
                        savedQuiz,
                        qr.getContent(),
                        qr.getCorrect(),
                        qr.getPoint()
                ))
                .collect(Collectors.toList());

        questionRepository.saveAll(questions);

        return new QuizResponse(
                savedQuiz.getQuizId(),
                savedQuiz.getQuizTitle(),
                savedQuiz.getQuizDueDate(),
                questions.stream()
                        .map(q-> new QuizResponse.QuestionResponse(
                                q.getQuestionId(),
                                q.getContent(),
                                q.getCorrect(),
                                q.getPoint()
                        ))
                        .collect(Collectors.toList())

        );
    }
}
