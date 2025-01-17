package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.dto.AssignmentRequest;
import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public Assignment createAssignment(Long courseId, AssignmentRequest assignmentRequest) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Assignment assignment = Assignment.of(
                course,
                assignmentRequest.getTitle(),
                assignmentRequest.getDescription(),
                assignmentRequest.getDueDate()
        );

        return assignmentRepository.save(assignment);
    }
}