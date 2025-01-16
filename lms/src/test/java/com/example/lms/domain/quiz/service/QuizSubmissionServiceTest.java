package com.example.lms.domain.quiz.service;

import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.question.repository.QuestionRepository;
import com.example.lms.domain.quiz.dto.QuizSubmissionRequest;
import com.example.lms.domain.quiz.dto.QuizSubmissionResponse;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class QuizSubmissionServiceTest {

    @Autowired QuizSubmissionService quizSubmissionService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testSubmitQuiz() {
        Student student = Student.of(
                "Student1",
                "password1",
                "test@example.com",
                "Test Student"
        );
        student = studentRepository.save(student);

        Quiz quiz = new Quiz();
        quiz.setQuizTitle("퀴즈 테스트");
        quiz.setCourseId(1L);
        quiz.setQuizDueDate(LocalDateTime.now().plusDays(1));
        quiz = quizRepository.save(quiz);

        Question question = new Question();
        question.setQuiz(quiz);
        question.setContent("2+2는?");
        question.setCorrect("4");
        question.setPoint(10);
        question = questionRepository.save(question);

        QuizSubmissionRequest request = new QuizSubmissionRequest();
        request.setStudentId(student.getId());
        Map<Long, String> answers = new HashMap<>();
        answers.put(question.getQuestionId(), "4");
        request.setAnswers(answers);

        QuizSubmissionResponse response = quizSubmissionService.submitQuiz(quiz.getQuizId(), request);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("퀴즈 답안이 성공적으로 제출되었습니다.", response.getMessage());


    }
}
