package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.dto.SubmissionRequest;
import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.entity.Submission;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.assignment.repository.SubmissionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class SubmissionServiceTest {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Assignment assignment;
    private Student student;

    @BeforeEach
    void setUp() {
        Course course = Course.of("Test Course", "Course Description", LocalDate.now(), LocalDate.now().plusDays(30), 30, null);
        entityManager.persist(course);
        entityManager.flush();
        entityManager.clear();

        assignment = Assignment.of(course, "Test Assignment", "Assignment Description", LocalDate.now().plusDays(7));
        assignment = assignmentRepository.save(assignment);

        student = Student.of("Test Student", "password", "student@example.com", "Test Student");
        student = studentRepository.save(student);
    }

    @Test
    @DisplayName("성공적으로 과제를 제출한다.")
    void testSubmitAssignment() {
        SubmissionRequest request = new SubmissionRequest();
        request.setStudentId(student.getId());
        request.setFileUrl("http://example.com/submission");

        Submission submission = submissionService.submitAssignment(assignment.getId(), request);

        assertNotNull(submission);
        assertEquals("http://example.com/submission", submission.getFileUrl());
        assertEquals(student.getId(), submission.getStudent().getId());
    }

    @Test
    @DisplayName("존재하지 않는 과제 제출 시 예외 발생")
    void testSubmitAssignmentNotFound() {
        SubmissionRequest request = new SubmissionRequest();
        request.setStudentId(student.getId());
        request.setFileUrl("http://example.com/submission");

        Long invalidAssignmentId = 999L;

        assertThrows(IllegalArgumentException.class, () ->
                        submissionService.submitAssignment(invalidAssignmentId, request),
                "Assignment not found");
    }
}
