package com.example.lms.domain.quiz.service;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.question.repository.QuestionRepository;
import com.example.lms.domain.quiz.dto.QuizSubmissionRequest;
import com.example.lms.domain.quiz.dto.QuizSubmissionResponse;
import com.example.lms.domain.quiz.entity.Quiz;
import com.example.lms.domain.quiz.repository.QuizRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.enums.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
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

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("퀴즈를 제출한다.")
    public void testSubmitQuiz() {
        Instructor instructor = Instructor.of(
                "instructor1",
                "password1",
                "instructor@example.com",
                "Test Instructor",
                "Test Instructor입니다"
        );
        instructor = instructorRepository.save(instructor);

        Course course = Course.of(
                "테스트 과정",
                "테스트 과정 설명",
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 25),
                30,
                instructor
        );
        course = courseRepository.save(course);

        Student student = Student.of(
                "Student1",
                "password1",
                "test@example.com",
                "Test Student"
        );
        student = studentRepository.save(student);

       Quiz quiz = Quiz.createQuiz(course, "테스트 퀴즈", LocalDateTime.now().plusDays(7));
       quiz = quizRepository.save(quiz);

       Question question = Question.createQuestion(quiz, "2+2는?", "4", 10);
       question = questionRepository.save(question);

        QuizSubmissionRequest request = new QuizSubmissionRequest();
        request.setStudentId(student.getId());
        Map<Long, String> answers = new HashMap<>();
        answers.put(question.getId(), "4");
        request.setAnswers(answers);

        QuizSubmissionResponse response = quizSubmissionService.submitQuiz(quiz.getId(), request);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("퀴즈 답안이 성공적으로 제출되었습니다.", response.getMessage());


    }
}
