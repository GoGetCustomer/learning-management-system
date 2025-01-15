package com.example.lms.domain.course.service;


import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.mapper.CourseMapper;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private Instructor instructor;
    private Instructor instructor2;
    private CourseFixture courseFixture;
    private CourseFixture courseFixture2;

    @BeforeEach
    void setUp() {
        instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        instructor2 = InstructorFixture.INSTRUCTOR_FIXTURE_2.createInstructor();
        courseFixture = CourseFixture.COURSE_FIXTURE_1;
        courseFixture2 = CourseFixture.COURSE_FIXTURE_2;
    }

    @Test
    @DisplayName("강의를 생성할 때 올바른 엔티티가 저장되고 응답 객체가 반환됩니다.")
    void createCourse_success() {
        // given
        CourseCreateRequestDto requestDto = courseFixture.toCreateRequestDto(1L);
        Course course = courseFixture.createCourse(instructor);
        CourseResponseDto responseDto = courseFixture.toResponseDto(1L, instructor.getName());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseMapper.toEntity(requestDto, instructor)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toResponseDto(course)).thenReturn(responseDto);

        // when
        CourseResponseDto result = courseService.createCourse(requestDto);

        // then
        assertSoftly((softly) -> {
            softly.assertThat(result.getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
            softly.assertThat(result.getCourseStudents()).isEqualTo(0);
            softly.assertThat(result.getStartDate()).isEqualTo(courseFixture.getStartDate());
            softly.assertThat(result.getEndDate()).isEqualTo(courseFixture.getEndDate());
            softly.assertThat(result.getCourseDescription()).isEqualTo(courseFixture.getCourseDescription());
            softly.assertThat(result.getInstructorName()).isEqualTo(instructor.getName());
        });
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("강사가 존재하지 않을 때 예외를 발생시킵니다.")
    void createCourse_instructorNotFound_throwsException() {
        // given
        CourseCreateRequestDto requestDto = courseFixture.toCreateRequestDto(1L);
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> courseService.createCourse(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 강사입니다.");
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("모든 강의 리스트를 조회할 수 있습니다.")
    void getAllCourses_success() {
        // given
        Course course1 = courseFixture.createCourse(instructor);
        Course course2 = courseFixture2.createCourse(instructor2);

        CourseResponseDto responseDto1 = courseFixture.toResponseDto(1L, instructor.getName());
        CourseResponseDto responseDto2 = courseFixture2.toResponseDto(2L, instructor2.getName());

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));
        when(courseMapper.toResponseDto(course1)).thenReturn(responseDto1);
        when(courseMapper.toResponseDto(course2)).thenReturn(responseDto2);

        // when
        List<CourseResponseDto> courses = courseService.getAllCourses(null);

        // then
        assertSoftly(softly -> {
            softly.assertThat(courses).hasSize(2);
            softly.assertThat(courses.get(0).getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
            softly.assertThat(courses.get(1).getCourseTitle()).isEqualTo(courseFixture2.getCourseTitle());
            softly.assertThat(courses.get(0).getInstructorName()).isEqualTo(instructor.getName());
            softly.assertThat(courses.get(1).getInstructorName()).isEqualTo(instructor2.getName());
        });
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 강사의 강의 리스트를 조회할 수 있습니다.")
    void getAllCoursesByInstructor_success() {
        // given
        Course course1 = courseFixture.createCourse(instructor);
        Course course2 = courseFixture2.createCourse(instructor);

        CourseResponseDto responseDto1 = courseFixture.toResponseDto(1L, instructor.getName());
        CourseResponseDto responseDto3 = courseFixture.toResponseDto(3L, instructor.getName());

        when(courseRepository.findByInstructor_Id(1L)).thenReturn(List.of(course1, course2));
        when(courseMapper.toResponseDto(course1)).thenReturn(responseDto1);
        when(courseMapper.toResponseDto(course2)).thenReturn(responseDto3);

        // when
        List<CourseResponseDto> courses = courseService.getAllCourses(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(courses).hasSize(2);
            softly.assertThat(courses.get(0).getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
            softly.assertThat(courses.get(1).getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
        });
        verify(courseRepository, times(1)).findByInstructor_Id(1L);
    }
}
