package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.entity.AssignmentGrade;
import com.example.lms.domain.assignment.repository.AssignmentGradeRepository;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class AssignmentGradeServiceTest {

    @Autowired
    private AssignmentGradeService gradeService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssignmentGradeRepository gradeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Assignment assignment;
    private Student student;

    @BeforeEach
    void setUp() {
        Course course = Course.of(
                "Test Course",
                "Course Description",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                30,
                null
        );
        entityManager.persist(course);
        entityManager.flush();
        entityManager.clear();

        assignment = Assignment.of(
                course,
                "Test Assignment",
                "Assignment Description",
                LocalDate.now().plusDays(7)
        );
        assignment = assignmentRepository.save(assignment);

        student = Student.of(
                "Test Student",
                "password",
                "student@example.com",
                "Test Student"
        );
        student = studentRepository.save(student);
    }

    @Test
    @DisplayName("성공적으로 성적을 생성한다.")
    void testCreateGrade() {
        AssignmentGrade grade = gradeService.createGrade(assignment.getId(), student.getId(), 90, "수고하셨습니다.");

        assertNotNull(grade);
        assertEquals(90, grade.getGrade());
        assertEquals("수고하셨습니다.", grade.getFeedback());
    }

    @Test
    @DisplayName("과제 ID로 성적 목록을 조회한다.")
    void testGetGradesByAssignment() {
        gradeService.createGrade(assignment.getId(), student.getId(), 85, "수고하셨습니다.");

        List<AssignmentGrade> grades = gradeService.getGradesByAssignment(assignment.getId());
        assertEquals(1, grades.size());
        assertEquals(85, grades.get(0).getGrade());
        assertEquals("수고하셨습니다.", grades.get(0).getFeedback());
    }
}
