package com.example.lms.domain.content.mapper;

import com.example.lms.domain.content.dto.response.ContentResponseDto;
import com.example.lms.domain.content.entity.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ContentMapper {

    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "fileUrl", target = "fileUrl")
    @Mapping(source = "course.id", target = "courseId")
    ContentResponseDto toResponse(Content content);

}