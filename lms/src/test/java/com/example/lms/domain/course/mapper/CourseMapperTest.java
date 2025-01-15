package com.example.lms.domain.course.mapper;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.entity.Instructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseMapperTest {
    private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @Test
    @DisplayName("CourseCreateRequestDto를 Course Entity로 변환합니다.")
    void toEntityTest() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        CourseFixture courseFixture = CourseFixture.COURSE_FIXTURE_1;
        CourseCreateRequestDto requestDto = courseFixture.toCreateRequestDto(1L);

        // when
        Course course = courseMapper.toEntity(requestDto, instructor);

        // then
        assertThat(course.getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
        assertThat(course.getCourseDescription()).isEqualTo(courseFixture.getCourseDescription());
        assertThat(course.getStartDate()).isEqualTo(courseFixture.getStartDate());
        assertThat(course.getEndDate()).isEqualTo(courseFixture.getEndDate());
        assertThat(course.getInstructor().getName()).isEqualTo(instructor.getName());
        assertThat(course.getCourseId()).isNull(); // courseId는 매핑에서 ignore 설정
    }

    @Test
    @DisplayName("Course Entity를 CourseResponseDto로 변환합니다.")
    void toResponseDtoTest() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        CourseFixture courseFixture = CourseFixture.COURSE_FIXTURE_2;
        Course course = courseFixture.createCourse(instructor);

        // when
        CourseResponseDto responseDto = courseMapper.toResponseDto(course);

        // then
        assertThat(responseDto.getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
        assertThat(responseDto.getInstructorName()).isEqualTo(instructor.getName());
        assertThat(responseDto.getCourseStudents()).isEqualTo(0); // constant 설정된 값
    }
}
