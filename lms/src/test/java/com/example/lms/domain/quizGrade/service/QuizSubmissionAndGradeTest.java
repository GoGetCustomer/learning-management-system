package com.example.lms.domain.quizGrade.service;

import com.example.lms.domain.answer.repository.AnswerRepository;
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
import com.example.lms.domain.quiz.service.QuizSubmissionService;
import com.example.lms.domain.quizGrade.repository.QuizGradeRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
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
public class QuizSubmissionAndGradeTest {

    @Autowired
    private QuizSubmissionService quizSubmissionService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuizGradeRepository quizGradeRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("여러 문제의 정답과 오답을 제출하여 점수를 계산한다.")
    public void testQuizWithCorrectAnswer() {
        Student student = Student.of("Student1", "password1", "test1@example.com", "Student1");
        student = studentRepository.save(student);

        Instructor instructor = Instructor.of(
                "Instructor1",
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

        Quiz quiz = Quiz.createQuiz(course, "테스트 퀴즈 1", LocalDateTime.now().plusDays(1));
        quiz = quizRepository.save(quiz);

        Question question1 = Question.createQuestion(quiz, "1 + 1은?", "2", 10);
        question1 = questionRepository.save(question1);

        Question question2 = Question.createQuestion(quiz, "2 + 2는?", "4", 20);
        question2 = questionRepository.save(question2);

        QuizSubmissionRequest request = new QuizSubmissionRequest();
        request.setStudentId(student.getId());
        Map<Long, String> answers = new HashMap<>();
        answers.put(question1.getId(), "2");
        answers.put(question2.getId(), "5");
        request.setAnswers(answers);

        QuizSubmissionResponse response = quizSubmissionService.submitQuiz(quiz.getId(), request);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("퀴즈 답안이 성공적으로 제출되었습니다.", response.getMessage());

        int totalPoints = question1.getPoint() + question2.getPoint();
        int correctPoints = 0;

        if (question1.getCorrect().equals(answers.get(question1.getId()))) {
            correctPoints += question1.getPoint();
        }

        if (question2.getCorrect().equals(answers.get(question2.getId()))) {
            correctPoints += question2.getPoint();
        }

        int expectedGrade = (correctPoints * 100) / totalPoints;
        var quizGrade = quizGradeRepository.findAll().get(0);
        assertEquals(expectedGrade, quizGrade.getGrade());
    }
}
