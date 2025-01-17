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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("local")
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
    @DisplayName("퀴즈를 제출한다.")
    public void testSubmitQuiz() {
        Student student = Student.of(
                "Student1",
                "password1",
                "test@example.com",
                "Test Student"
        );
        student = studentRepository.save(student);

       Quiz quiz = Quiz.createQuiz(1L, "테스트 퀴즈", LocalDateTime.now().plusDays(7));
       quiz = quizRepository.save(quiz);

       Question question = Question.createQuestion(quiz, "2+2는?", "4", 10);
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
