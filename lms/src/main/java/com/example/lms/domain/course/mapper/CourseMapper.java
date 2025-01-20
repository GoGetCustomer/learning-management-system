package com.example.lms.domain.course.mapper;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.dto.InstructorInfo;
import com.example.lms.domain.instructor.entity.Instructor;
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

//    @Mapping(target = "courseStudents", expression = "java(course.getRegistrations().size())")
//    @Mapping(target = "instructor", expression = "java(course.getTeachings().isEmpty() ? null : toInstructorInfo(course.getTeachings().get(0).getInstructor()))")
//    CourseResponseDto toResponseDto(Course course);

    InstructorInfo toInstructorInfo(Instructor instructor);

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
