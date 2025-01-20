package com.example.lms.domain.course.mapper;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CourseMapperTest {
    private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @Test
    @DisplayName("CourseCreateRequestDto를 Course Entity로 변환합니다.")
    void toEntityTest() {
        // given
        CourseFixture courseFixture = CourseFixture.COURSE_FIXTURE_1;
        CourseCreateRequestDto requestDto = courseFixture.toCreateRequestDto(1L);

        // when
        Course course = courseMapper.toEntity(requestDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(course.getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
            softly.assertThat(course.getCourseDescription()).isEqualTo(courseFixture.getCourseDescription());
            softly.assertThat(course.getStartDate()).isEqualTo(courseFixture.getStartDate());
            softly.assertThat(course.getEndDate()).isEqualTo(courseFixture.getEndDate());
            softly.assertThat(course.getId()).isNull();
        });

    }

    @Test
    @DisplayName("CourseUpdateRequestDto를 Course Entity로 변환합니다.")
    void toUpdateEntityTest() {
        // given
        CourseFixture courseFixture = CourseFixture.COURSE_FIXTURE_2;
        CourseUpdateRequestDto updateDto = CourseUpdateRequestDto.builder()
                .courseTitle(courseFixture.getCourseTitle())
                .courseDescription(courseFixture.getCourseDescription())
                .startDate(courseFixture.getStartDate())
                .endDate(courseFixture.getEndDate())
                .courseCapacity(courseFixture.getCourseCapacity())
                .build();

        // when
        Course course = courseMapper.toUpdateEntity(updateDto);

        // then
        assertSoftly(softly -> {
            softly.assertThat(course.getCourseTitle()).isEqualTo(courseFixture.getCourseTitle());
            softly.assertThat(course.getCourseDescription()).isEqualTo(courseFixture.getCourseDescription());
            softly.assertThat(course.getStartDate()).isEqualTo(courseFixture.getStartDate());
            softly.assertThat(course.getEndDate()).isEqualTo(courseFixture.getEndDate());
            softly.assertThat(course.getId()).isNull(); // courseId는 무시됨
        });
    }

//    @Test
//    @DisplayName("Course Entity를 CourseResponseDto로 변환합니다.")
//    void toResponseDtoTest() {
//        // given
//        Course course = CourseFixture.COURSE_FIXTURE_2.createCourse();
//
//        // when
//        CourseResponseDto responseDto = courseMapper.toResponseDto(course);
//
//        // then
//        assertSoftly(softly -> {
//            softly.assertThat(responseDto.getCourseTitle()).isEqualTo(course.getCourseTitle());
//            softly.assertThat(responseDto.getCourseStudents()).isEqualTo(0); // constant 설정된 값
//        });
//    }

    @Test
    @DisplayName("Course Entity를 CourseCreateResponseDto로 변환합니다.")
    void toCreateResponseDtoTest() {
        // given
        Course course = CourseFixture.COURSE_FIXTURE_1.createCourse();

        // when
        CourseCreateResponseDto responseDto = courseMapper.toCreateResponseDto(course);

        // then
        assertSoftly(softly -> {
            softly.assertThat(responseDto.getCourseTitle()).isEqualTo(course.getCourseTitle());
            softly.assertThat(responseDto.getCourseStudents()).isEqualTo(0); // constant 설정된 값
        });
    }

    @Test
    @DisplayName("CourseUpdateRequestDto를 사용해 Course 엔티티를 업데이트합니다.")
    void updateEntityFromDtoTest() {
        Course course = CourseFixture.COURSE_FIXTURE_1.createCourse();

        CourseUpdateRequestDto updateDto = CourseUpdateRequestDto.builder()
                .courseTitle("New Title")
                .courseDescription("New Description")
                .startDate(LocalDate.of(2025, 2, 1))
                .endDate(LocalDate.of(2025, 4, 1))
                .courseCapacity(150)
                .build();

        courseMapper.updateEntityFromDto(updateDto, course);

        assertSoftly(softly -> {
            softly.assertThat(course.getCourseTitle()).isEqualTo("New Title");
            softly.assertThat(course.getCourseDescription()).isEqualTo("New Description");
            softly.assertThat(course.getStartDate()).isEqualTo(LocalDate.of(2025, 2, 1));
            softly.assertThat(course.getEndDate()).isEqualTo(LocalDate.of(2025, 4, 1));
            softly.assertThat(course.getCourseCapacity()).isEqualTo(150);
        });
    }

    @Test
    @DisplayName("Course Entity를 CourseUpdateResponseDto로 변환합니다.")
    void toUpdateResponseDtoTest() {
        // given
        Course course = CourseFixture.COURSE_FIXTURE_1.createCourse();

        // when
        CourseUpdateResponseDto responseDto = courseMapper.toUpdateResponseDto(course);

        // then
        assertSoftly(softly -> {
            softly.assertThat(responseDto.getCourseTitle()).isEqualTo(course.getCourseTitle());
            softly.assertThat(responseDto.getCourseDescription()).isEqualTo(course.getCourseDescription());
        });
    }
}
