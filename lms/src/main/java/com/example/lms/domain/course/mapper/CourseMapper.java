package com.example.lms.domain.course.mapper;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.entity.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    @Mapping(target = "courseId", ignore = true)
    Course toEntity(CourseCreateRequestDto dto, Instructor instructor);


    @Mapping(target = "instructorName", source = "instructor.name")
//    @Mapping(target = "courseStudents", expression = "java(course.getRegistrations().size())")  // 수강생 수 계산
    @Mapping(target = "courseStudents", constant = "0")
    CourseResponseDto toResponseDto(Course course);
}
