package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.dto.AssignmentDto;
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

    public Assignment createAssignment(Long courseId, AssignmentDto assignmentDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setDueDate(assignmentDto.getDueDate());

        return assignmentRepository.save(assignment);
    }
}
