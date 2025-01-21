package com.example.lms.domain.course.mapper;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.dto.InstructorInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseCreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    Course toUpdateEntity(CourseUpdateRequestDto dto);

    @Mapping(target = "courseStudents", constant = "0")
    CourseCreateResponseDto toCreateResponseDto(Course course);

    CourseUpdateResponseDto toUpdateResponseDto(Course course);

    @Mapping(target = "instructorInfo", source = "instructorInfo")
    @Mapping(target = "courseStudents", expression = "java(course.getRegistrations() != null ? course.getRegistrations().size() : 0)")
    CourseResponseDto toResponseDto(Course course, InstructorInfo instructorInfo);

    default void updateEntityFromDto(CourseUpdateRequestDto dto, @MappingTarget Course course) {
        course.updateCourse(
                dto.getCourseTitle(),
                dto.getCourseDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCourseCapacity()
        );
    }
}
