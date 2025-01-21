package com.example.lms.domain.course.service;


import com.example.lms.common.config.WithMockCustom;
import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
    private Course course1;
    private Course course2;
    @BeforeEach
    void setUp() {
        instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        course1 = CourseFixture.COURSE_FIXTURE_1.createCourse();
        course2 = CourseFixture.COURSE_FIXTURE_2.createCourse();
    }

    @Test
    @DisplayName("과정을 성공적으로 생성합니다.")
    void createCourse_success() {
        // given
        String loginId = "testLoginId";
        CourseCreateRequestDto requestDto = CourseFixture.COURSE_FIXTURE_1.toCreateRequestDto(1L);

        UserDetails userDetails = User.withUsername(loginId)
                .password("password1234@")
                .roles("INSTRUCTOR")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(instructorRepository.findByLoginIdAndNotDeleted(loginId))
                .thenReturn(Optional.of(instructor));

        when(courseMapper.toEntity(requestDto)).thenReturn(course1);
        when(courseRepository.save(any(Course.class))).thenReturn(course1);
        when(courseMapper.toCreateResponseDto(course1))
                .thenReturn(CourseFixture.COURSE_FIXTURE_1.toCreateResponseDto(1L));

        // when
        CourseCreateResponseDto response = courseService.createCourse(requestDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getId()).isEqualTo(1L);
            softly.assertThat(response.getCourseTitle())
                    .isEqualTo(CourseFixture.COURSE_FIXTURE_1.getCourseTitle());
        });

        verify(instructorRepository, times(1)).findByLoginIdAndNotDeleted(loginId);
        verify(courseMapper, times(1)).toEntity(requestDto);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @WithMockCustom(id = 1L, role = "ROLE_INSTRUCTOR")
    @DisplayName("강좌를 성공적으로 업데이트합니다.")
    void updateCourse_success() {
        // given
        Long courseId = 1L;
        CourseUpdateRequestDto requestDto = CourseFixture.COURSE_FIXTURE_2.toUpdateRequestDto(courseId);
        String loginId = "testLoginId";

        UserDetails userDetails = User.withUsername(loginId)
                .password("password1234@")
                .roles("INSTRUCTOR")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Teaching teaching = Teaching.of(instructor, course1);
        course1.addTeaching(teaching);

        when(instructorRepository.findByLoginIdAndNotDeleted("testLoginId"))
                .thenReturn(Optional.of(instructor));
        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course1));
        when(courseRepository.save(any(Course.class)))
                .thenReturn(course1);
        when(courseMapper.toUpdateResponseDto(course1))
                .thenReturn(CourseFixture.COURSE_FIXTURE_2.toUpdateResponseDto(courseId));

        // when
        CourseUpdateResponseDto response = courseService.updateCourse(courseId, requestDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getId()).isEqualTo(courseId);
            softly.assertThat(response.getCourseTitle()).isEqualTo(CourseFixture.COURSE_FIXTURE_2.getCourseTitle());
        });

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("강좌를 성공적으로 삭제합니다.")
    void deleteCourse_success() {
        // given
        Long courseId = 1L;
        String loginId = "testLoginId";

        UserDetails userDetails = User.withUsername(loginId)
                .password("password1234@")
                .roles("INSTRUCTOR")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Teaching teaching = Teaching.of(instructor, course1);
        course1.addTeaching(teaching);
        assertThat(course1.getTeachings()).hasSize(1);
        when(instructorRepository.findByLoginIdAndNotDeleted(loginId))
                .thenReturn(Optional.of(instructor));
        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course1));

        // when

        courseService.deleteCourse(courseId);

        // then
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course1);
        assertSoftly(softly -> {
            softly.assertThat(course1.getTeachings()).isEmpty();
        });
    }
    @Test
    @DisplayName("특정 courseId로 Course 조회")
    void getCourseById_success() {
        // given
        when(courseRepository.findById(course1.getId())).thenReturn(Optional.of(course1));
        InstructorInfo instructorInfo = InstructorInfo.builder()
                .instructorId(instructor.getId())
                .name(instructor.getName())
                .email(instructor.getEmail())
                .description(instructor.getDescription())
                .build();

        when(courseMapper.toResponseDto(eq(course1), any())).thenReturn(CourseFixture.COURSE_FIXTURE_1.toResponseDto(course1, instructorInfo));

        // when
        CourseResponseDto responseDto = courseService.getCourseById(course1.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(responseDto.getId()).isEqualTo(course1.getId());
            softly.assertThat(responseDto.getCourseTitle()).isEqualTo(course1.getCourseTitle());
        });

        verify(courseRepository).findById(course1.getId());
        verify(courseMapper).toResponseDto(eq(course1), any());
    }

    @Test
    @DisplayName("특정 instructorId로 Course 목록 조회")
    void getCourseByInstructorId_success() {
        // given
        when(instructorRepository.findById(1L))
                .thenReturn(Optional.of(instructor));
        when(courseRepository.findAllByInstructorId(1L))
                .thenReturn(List.of(course1, course2));
        InstructorInfo instructorInfo = InstructorInfo.builder()
                .instructorId(1L)
                .name(instructor.getName())
                .email(instructor.getEmail())
                .description(instructor.getDescription())
                .build();
        when(courseMapper.toResponseDto(eq(course1), any()))
                .thenReturn(CourseFixture.COURSE_FIXTURE_1.toResponseDto(course1, instructorInfo));
        when(courseMapper.toResponseDto(eq(course2), any()))
                .thenReturn(CourseFixture.COURSE_FIXTURE_2.toResponseDto(course2, instructorInfo));

        // when
        List<CourseResponseDto> responses = courseService.getCourseByInstructorId(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(2);
            softly.assertThat(responses.get(0).getId()).isEqualTo(course1.getId());
            softly.assertThat(responses.get(1).getId()).isEqualTo(course2.getId());
        });

        verify(instructorRepository).findById(1L);
        verify(courseRepository).findAllByInstructorId(1L);
        verify(courseMapper).toResponseDto(eq(course1), any());
        verify(courseMapper).toResponseDto(eq(course2), any());
    }

    @Test
    @DisplayName("instructorId가 null일 경우 모든 Course 목록 조회")
    void getCourseByInstructorId_nullInstructorId_success() {
        // given
        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
        InstructorInfo instructorInfo = InstructorInfo.builder()
                .instructorId(instructor.getId())
                .name(instructor.getName())
                .email(instructor.getEmail())
                .description(instructor.getDescription())
                .build();
        when(courseMapper.toResponseDto(eq(course1), any())).thenReturn(CourseFixture.COURSE_FIXTURE_1.toResponseDto(course1, instructorInfo));
        when(courseMapper.toResponseDto(eq(course2), any())).thenReturn(CourseFixture.COURSE_FIXTURE_2.toResponseDto(course2, instructorInfo));

        // when
        List<CourseResponseDto> responseDtos = courseService.getCourseByInstructorId(null);

        // then
        assertSoftly(softly -> {
            softly.assertThat(responseDtos).hasSize(2);
            softly.assertThat(responseDtos.get(0).getId()).isEqualTo(course1.getId());
            softly.assertThat(responseDtos.get(1).getId()).isEqualTo(course2.getId());
        });

        verify(courseRepository).findAll();
        verify(courseMapper).toResponseDto(eq(course1), any());
        verify(courseMapper).toResponseDto(eq(course2), any());
    }

}
