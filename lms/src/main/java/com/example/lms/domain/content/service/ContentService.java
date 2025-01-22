package com.example.lms.domain.content.service;

import com.example.lms.common.service.CourseValidationService;
import com.example.lms.common.service.FileService;
import com.example.lms.domain.content.dto.response.ContentResponseDto;
import com.example.lms.domain.content.entity.Content;
import com.example.lms.domain.content.mapper.ContentMapper;
import com.example.lms.domain.content.repository.ContentRepository;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.instructor.entity.Instructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final CourseValidationService courseValidationService;
    private final FileService fileService;
    private final ContentMapper contentMapper;

    @Transactional
    public ContentResponseDto addContent(Long courseId, MultipartFile multipartFile) throws IOException {
        Instructor instructor = courseValidationService.validateInstructor();
        Course course = courseValidationService.validateCourseInstructor(courseId, instructor);
        String fileName = fileService.generateUniqueFileName(multipartFile.getOriginalFilename());
        String fileType = multipartFile.getContentType();
        String fileUrl = fileService.generateS3FileUrl(fileName);

        fileService.uploadToS3(multipartFile, fileName);

        Content content = Content.of(course,fileType, fileName, fileUrl);
        contentRepository.save(content);

        return contentMapper.toResponse(content);
    }

    @Transactional
    public void deleteContent(Long courseId, Long contentId) {
        Instructor instructor = courseValidationService.validateInstructor();
        Course course = courseValidationService.validateCourseInstructor(courseId, instructor);
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의자료가 존재하지 않습니다."));

        contentRepository.delete(content);
        fileService.deleteFromS3(content.getFileUrl());
    }

}
