package com.example.lms.domain.course.service;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.mapper.CourseMapper;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.teaching.entity.Teaching;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseCreateResponseDto createCourse(CourseCreateRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Instructor instructor = instructorRepository.findByLoginIdAndNotDeleted(loginId)
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
        String loginId = authentication.getName();

        Instructor instructor = instructorRepository.findByLoginIdAndNotDeleted(loginId)
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
        String loginId = authentication.getName();

        Instructor instructor = instructorRepository.findByLoginIdAndNotDeleted(loginId)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 강좌를 삭제할 권한이 없습니다.");
        }
        course.getTeachings().clear();
        courseRepository.delete(course);
    }

}
