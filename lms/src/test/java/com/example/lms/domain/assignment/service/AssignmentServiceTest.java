package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.dto.AssignmentRequest;
import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
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
class AssignmentServiceTest {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Long courseId;

    @BeforeEach
    void setUp() {
        Course course = Course.of("Test Course", "Course Description", LocalDate.now(), LocalDate.now().plusDays(30), 30, null);
        course = courseRepository.save(course);
        this.courseId = course.getId();
    }

    @Test
    @DisplayName("성공적으로 과제를 생성한다.")
    void testCreateAssignment() {
        AssignmentRequest request = new AssignmentRequest("New Assignment", "Assignment Description", LocalDate.now().plusDays(7));
        Assignment result = assignmentService.createAssignment(courseId, request);

        assertNotNull(result);
        assertEquals("New Assignment", result.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 과정으로 과제를 생성할 때 예외 발생")
    void testCreateAssignmentCourseNotFound() {
        AssignmentRequest request = new AssignmentRequest("Invalid Assignment", "Description", LocalDate.now().plusDays(7));
        Long invalidCourseId = 999L;

        assertThrows(IllegalArgumentException.class, () ->
                        assignmentService.createAssignment(invalidCourseId, request),
                "Course not found");
    }
}
