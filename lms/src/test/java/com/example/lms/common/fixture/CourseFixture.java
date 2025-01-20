package com.example.lms.common.fixture;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum CourseFixture {
    COURSE_FIXTURE_1(
            "Java Spring Boot Course",
            "스프링 부트의 기초를 학습하고 직접 프로젝트를 진행해보는 효율적인 강의입니다.",
            LocalDate.of(2025, 1, 23),
            LocalDate.of(2025, 3, 4),
            30),
    COURSE_FIXTURE_2(
            "Data Structures and Algorithms",
            "자료구와 알고리즘에 대해서 꼼꼼하게 학습하는 강의입니다.",
            LocalDate.of(2025, 2, 25),
            LocalDate.of(2025, 4, 3),
            50);

    private final String courseTitle;
    private final String courseDescription;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer courseCapacity;

    CourseFixture(String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, int courseCapacity) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseCapacity = courseCapacity;
    }

    public Course createCourse() {
        return Course.builder()
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .build();
    }

    public CourseCreateRequestDto toCreateRequestDto(Long courseId) {
        return CourseCreateRequestDto.builder()
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .build();
    }

    public CourseResponseDto toResponseDto(Long courseId) {
        return CourseResponseDto.builder()
                .courseId(courseId)
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseStudents(0)
                .build();
    }

    public CourseCreateResponseDto toCreateResponseDto(Long courseId) {
        return CourseCreateResponseDto.builder()
                .id(courseId)
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseStudents(0)
                .build();
    }

    public CourseUpdateResponseDto toUpdateResponseDto(Long courseId) {
        return CourseUpdateResponseDto.builder()
                .id(courseId)
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .build();
    }

    public CourseUpdateRequestDto toUpdateRequestDto(Long courseId) {
        return CourseUpdateRequestDto.builder()
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .build();
    }
}