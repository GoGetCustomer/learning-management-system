package com.example.lms.domain.quiz.service;

import com.example.lms.domain.answer.entity.Answer;
import com.example.lms.domain.answer.repository.AnswerRepository;
import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.question.repository.QuestionRepository;
import com.example.lms.domain.quiz.dto.QuizSubmissionRequest;
import com.example.lms.domain.quiz.dto.QuizSubmissionResponse;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.quizGrade.entity.QuizGrade;
import com.example.lms.domain.quizGrade.repository.QuizGradeRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizSubmissionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizGradeRepository quizGradeRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public QuizSubmissionResponse submitQuiz(Long quizId, QuizSubmissionRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        int totalPoints = 0;
        int correctPoints = 0;

        for (Map.Entry<Long, String> entry : request.getAnswers().entrySet()) {
            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

            if (!question.getQuiz().getId().equals(quizId)) {
                throw new IllegalArgumentException("질문이 해당 퀴즈에 속하지 않습니다.");
            }

            boolean isCorrect = question.getCorrect().equals(submittedAnswer);
            totalPoints += question.getPoint();
            if (isCorrect) {
                correctPoints += question.getPoint();
            }

            Answer answer = Answer.createAnswer(submittedAnswer, student, question);
            answerRepository.save(answer);
        }

            int grade = totalPoints > 0 ? (correctPoints * 100) / totalPoints : 0;
            QuizGrade quizGrade = QuizGrade.create(student, quiz, grade);
            quizGradeRepository.save(quizGrade);

            return QuizSubmissionResponse.builder()
                    .status("success")
                    .message("퀴즈 답안이 성공적으로 제출되었습니다.")
                    .totalScore(totalPoints)
                    .correctScore(correctPoints)
                    .build();
        }
}
