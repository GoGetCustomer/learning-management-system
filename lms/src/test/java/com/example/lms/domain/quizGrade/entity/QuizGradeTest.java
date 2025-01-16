package com.example.lms.domain.quizGrade.entity;

import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.quizGrade.repository.QuizGradeRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuizGradeTest {

    @Autowired
    private QuizGradeRepository quizGradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    @Transactional
    @DisplayName("퀴즈성적 엔티티 저장 및 연관관계 테스트")
    void testQuizGradeEntity() {

        Student student = Student.of("loginId", "password123", "student@example.com", "홍길동");
        studentRepository.save(student);

        Quiz quiz = Quiz.createQuiz(1L, "Sample Quiz", LocalDateTime.now().plusDays(7));
        quizRepository.save(quiz);

        QuizGrade quizGrade = QuizGrade.create(student, quiz, (byte) 90);
        QuizGrade savedQuizGrade = quizGradeRepository.save(quizGrade);

        assertThat(savedQuizGrade.getQuizGradeId()).isNotNull();
        assertThat(savedQuizGrade.getStudent()).isEqualTo(student);
        assertThat(savedQuizGrade.getQuiz()).isEqualTo(quiz);
        assertThat(savedQuizGrade.getGrade()).isEqualTo((byte) 90);
    }
}
