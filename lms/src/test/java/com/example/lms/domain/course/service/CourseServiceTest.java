package com.example.lms.domain.course.service;


import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        course = CourseFixture.COURSE_FIXTURE_1.createCourse();
    }

//    @Test
//    @DisplayName("과정을 성공적으로 생성합니다.")
//    @WithMockUser(username = "testLoginId", roles = "ROLE_INSTRUCTOR")
//    void createCourse_success() {
//        // given
//        CourseCreateRequestDto requestDto = CourseFixture.COURSE_FIXTURE_1.toCreateRequestDto(1L);
//
//        // Mock 설정
//        when(instructorRepository.findByLoginIdAndNotDeleted("testLoginId"))
//                .thenReturn(Optional.of(instructor));
//        when(courseMapper.toEntity(requestDto)).thenReturn(course);
//        when(courseRepository.save(any(Course.class))).thenReturn(course);
//        when(courseMapper.toCreateResponseDto(course)).thenReturn(CourseFixture.COURSE_FIXTURE_1.toCreateResponseDto(1L));
//
//        // when
//        CourseCreateResponseDto response = courseService.createCourse(requestDto);
//
//        // then
//        assertSoftly(softly -> {
//            softly.assertThat(response).isNotNull();
//            softly.assertThat(response.getCourseId()).isEqualTo(1L);
//            softly.assertThat(response.getCourseTitle()).isEqualTo(CourseFixture.COURSE_FIXTURE_1.getCourseTitle());
//        });
//
//        verify(instructorRepository, times(1)).findByLoginIdAndNotDeleted("testLoginId");
//        verify(courseMapper, times(1)).toEntity(requestDto);
//        verify(courseRepository, times(1)).save(any(Course.class));
//    }
}
