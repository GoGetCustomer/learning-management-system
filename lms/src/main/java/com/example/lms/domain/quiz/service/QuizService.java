package com.example.lms.domain.quiz.service;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
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
    private final CourseRepository courseRepository;

    @Transactional
    public QuizResponse createQuiz(QuizRequest quizRequest) {
        Course course = courseRepository.findById(quizRequest.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 과정을 찾을 수 없습니다."));

        Quiz quiz = Quiz.createQuiz(
                course,
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
                savedQuiz.getId(),
                savedQuiz.getQuizTitle(),
                savedQuiz.getQuizDueDate(),
                questions.stream()
                        .map(q-> new QuizResponse.QuestionResponse(
                                q.getId(),
                                q.getContent(),
                                q.getCorrect(),
                                q.getPoint()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

        return new QuizResponse(
                quiz.getId(),
                quiz.getQuizTitle(),
                quiz.getQuizDueDate(),
                quiz.getQuestions().stream()
                        .map(q -> new QuizResponse.QuestionResponse(
                                q.getId(),
                                q.getContent(),
                                q.getCorrect(),
                                q.getPoint()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzesByCourseId(Long courseId) {
        List<Quiz> quizzes = quizRepository.findAllByCourseId(courseId);

        return quizzes.stream()
                .map(quiz -> new QuizResponse(
                        quiz.getId(),
                        quiz.getQuizTitle(),
                        quiz.getQuizDueDate(),
                        quiz.getQuestions().stream()
                                .map(q -> new QuizResponse.QuestionResponse(
                                        q.getId(),
                                        q.getContent(),
                                        q.getCorrect(),
                                        q.getPoint()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizResponse updateQuiz(Long quizId, QuizRequest quizRequest) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

        quiz.updateQuizInfo(quizRequest.getQuizTitle(), quizRequest.getQuizDueDate());

        questionRepository.deleteAll(quiz.getQuestions());
        quiz.getQuestions().clear();

        List<Question> updatedQuestions = quizRequest.getQuestions().stream()
                .map(qr -> Question.createQuestion(quiz, qr.getContent(), qr.getCorrect(), qr.getPoint()))
                .collect(Collectors.toList());
        questionRepository.saveAll(updatedQuestions);

        return new QuizResponse(
                quiz.getId(),
                quiz.getQuizTitle(),
                quiz.getQuizDueDate(),
                updatedQuestions.stream()
                        .map(q -> new QuizResponse.QuestionResponse(
                                q.getId(),
                                q.getContent(),
                                q.getCorrect(),
                                q.getPoint()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

        quizRepository.delete(quiz);
    }
}
