package com.example.lms.domain.teaching.service;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.teaching.dto.TeachingResponseDto;
import com.example.lms.domain.teaching.entity.Teaching;
import com.example.lms.domain.teaching.repository.TeachingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeachingServiceTest {

    @Autowired
    TeachingService teachingService;

    @Autowired
    TeachingRepository teachingRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    CourseRepository courseRepository;

    @Test
    @DisplayName("수업 목록을 조회합니다.")
    void findAllTeachingTest() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(instructor.getId(), null));
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Teaching teaching = teachingRepository.save(Teaching.of(instructor, course));

        //when
        Page<TeachingResponseDto> result = teachingService.findAllTeaching(1);

        //then
        TeachingResponseDto dto = result.getContent().get(0);
        assertAll(
                () -> assertThat(dto.getId()).isEqualTo(teaching.getId()),
                () -> assertThat(dto.getInstructorName()).isEqualTo(instructor.getName()),
                () -> assertThat(dto.getInstructorEmail()).isEqualTo(instructor.getEmail()),
                () -> assertThat(dto.getCourseId()).isEqualTo(course.getId()),
                () -> assertThat(dto.getCourseTitle()).isEqualTo(course.getCourseTitle()),
                () -> assertThat(dto.getCourseDescription()).isEqualTo(course.getCourseDescription()),
                () -> assertThat(dto.getStartDate()).isEqualTo(course.getStartDate()),
                () -> assertThat(dto.getEndDate()).isEqualTo(course.getEndDate())
        );
    }
}