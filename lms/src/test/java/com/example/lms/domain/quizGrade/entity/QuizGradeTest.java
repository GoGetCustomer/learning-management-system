package com.example.lms.domain.quizGrade.entity;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.quizGrade.repository.QuizGradeRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class QuizGradeTest {

    @Autowired
    private QuizGradeRepository quizGradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @Transactional
    @DisplayName("퀴즈성적 엔티티 저장 및 연관관계 테스트")
    void testQuizGradeEntity() {

        Student student = Student.of("loginId", "password123", "student@example.com", "홍길동");
        studentRepository.save(student);

        Instructor instructor = Instructor.of(
                "Instructor1",
                "password1",
                "instructor@example.com",
                "Test Instructor",
                "Test Instructor입니다"
        );
        instructor = instructorRepository.save(instructor);

        Course course = Course.of("테스트 과정", "테스트 과정 설명", LocalDate.of(2025,1,20 ), LocalDate.of(2025, 1, 25), 30, instructor);
        courseRepository.save(course);

        Quiz quiz = Quiz.createQuiz(course, "Sample Quiz", LocalDateTime.now().plusDays(7));
        quizRepository.save(quiz);

        QuizGrade quizGrade = QuizGrade.create(student, quiz, (byte) 90);
        QuizGrade savedQuizGrade = quizGradeRepository.save(quizGrade);

        assertThat(savedQuizGrade.getId()).isNotNull();
        assertThat(savedQuizGrade.getStudent()).isEqualTo(student);
        assertThat(savedQuizGrade.getQuiz()).isEqualTo(quiz);
        assertThat(savedQuizGrade.getGrade()).isEqualTo((byte) 90);
    }
}
