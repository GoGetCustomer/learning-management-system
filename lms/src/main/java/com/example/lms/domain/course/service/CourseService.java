package com.example.lms.domain.course.service;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.mapper.CourseMapper;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseResponseDto createCourse(CourseCreateRequestDto requestDto) {
        Instructor instructor = instructorRepository.findById(requestDto.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강사입니다."));

        Course course = courseMapper.toEntity(requestDto, instructor);
        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponseDto(savedCourse);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCourses(Long instructorId) {
        List<Course> courses = (instructorId == null)
                ? courseRepository.findAll()
                : courseRepository.findByInstructor_Id(instructorId);

        return courses.stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
