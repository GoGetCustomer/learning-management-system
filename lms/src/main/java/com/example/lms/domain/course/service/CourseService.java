package com.example.lms.domain.course.service;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.mapper.CourseMapper;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.dto.InstructorInfo;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.teaching.entity.Teaching;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public CourseCreateResponseDto createCourse(CourseCreateRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        Instructor instructor = instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));

        Course course = courseMapper.toEntity(requestDto);

        Teaching teaching = Teaching.of(instructor, course);
        course.addTeaching(teaching);

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toCreateResponseDto(savedCourse);
    }
    @Transactional
    public CourseUpdateResponseDto updateCourse(Long courseId, CourseUpdateRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        Instructor instructor = instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        // 강좌의 소유권 확인
        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 강좌를 수정할 권한이 없습니다.");
        }

        courseMapper.updateEntityFromDto(requestDto, course);

        // 업데이트된 강좌 저장
        Course savedCourse = courseRepository.save(course);

        return courseMapper.toUpdateResponseDto(savedCourse);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        Instructor instructor = instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 강좌를 삭제할 권한이 없습니다.");
        }
        course.getTeachings().clear();
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(Long courseId) {
        // courseId로 Course 조회
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        InstructorInfo instructorInfo = course.getTeachings().stream()
                .findFirst()
                .map(teaching -> InstructorInfo.builder()
                        .instructorId(teaching.getInstructor().getId())
                        .name(teaching.getInstructor().getName())
                        .email(teaching.getInstructor().getEmail())
                        .description(teaching.getInstructor().getDescription())
                        .build())
                .orElse(null);

        return courseMapper.toResponseDto(course, instructorInfo);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCourseByInstructorId(Long instructorId) {
        List<Course> courses;
        if (instructorId == null) {
            courses = courseRepository.findAll();
        } else {
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

            courses = courseRepository.findAllByInstructorId(instructorId);

            if (courses.isEmpty()) {
                throw new IllegalArgumentException("해당 강사가 담당하는 과정이 없습니다.");
            }
        }

        return courses.stream()
                .map(course -> {
                    Instructor instructor = course.getTeachings()
                            .stream()
                            .findFirst() // 강의는 하나의 강사에게만 귀속된다는 가정
                            .map(Teaching::getInstructor)
                            .orElse(null);

                    InstructorInfo instructorInfo = instructor != null
                            ? InstructorInfo.builder()
                            .instructorId(instructor.getId())
                            .name(instructor.getName())
                            .email(instructor.getEmail())
                            .description(instructor.getDescription())
                            .build()
                            : null;

                    return courseMapper.toResponseDto(course, instructorInfo);
                })
                .collect(Collectors.toList());
    }

}
