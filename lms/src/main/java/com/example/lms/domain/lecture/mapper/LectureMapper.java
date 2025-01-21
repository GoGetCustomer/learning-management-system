package com.example.lms.domain.lecture.mapper;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.entity.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface LectureMapper {

    @Mapping(target = "lectureUrl", source = "lectureUrl")
    @Mapping(target = "lectureTime", source = "lectureTime")
    Lecture toEntity(LectureCreateRequestDto request, Course course, String lectureUrl, LocalTime lectureTime);

    @Mapping(source = "lectureTitle", target = "lectureTitle")
    @Mapping(source = "lectureDescription", target = "lectureDescription")
    @Mapping(source = "lectureUrl", target = "lectureUrl")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "lectureTime", target = "lectureTime")
    LectureCreateResponseDto toResponse(Lecture lecture);

}