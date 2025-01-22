package com.example.lms.common.service;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseValidationService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    public Instructor validateInstructor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        return instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));
    }

    public Course validateCourseInstructor(Long courseId, Instructor instructor) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 강좌에 강의 또는 강의 자료를 추가할 수 없습니다.");
        }

        return course;
    }
    public Course validateCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        return course;
    }
}