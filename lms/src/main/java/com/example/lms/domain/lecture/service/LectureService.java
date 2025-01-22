package com.example.lms.domain.lecture.service;

import com.example.lms.common.service.CourseValidationService;
import com.example.lms.common.service.FileService;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.entity.Lecture;
import com.example.lms.domain.lecture.mapper.LectureMapper;
import com.example.lms.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;
    private final LectureMapper lectureMapper;
    private final InstructorRepository instructorRepository;
    private final FileService fileService;
    private final CourseValidationService courseValidationService;

    @Transactional
    public LectureCreateResponseDto createLecture(LectureCreateRequestDto request,
                                                  Long courseId, MultipartFile multipartFile) throws IOException {

        Instructor instructor = courseValidationService.validateInstructor();
        Course course = courseValidationService.validateCourseInstructor(courseId, instructor);

        String fileName = fileService.generateUniqueFileName(request.getLectureTitle());
        fileService.uploadToS3(multipartFile, fileName);

        String fileUrl = fileService.generateS3FileUrl(fileName);

        // 강의 시간 설정 (예제 값 사용)
        LocalTime lectureTime = LocalTime.of(0, 10);

        Lecture lecture = lectureMapper.toEntity(request, course, fileUrl, lectureTime);

        lectureRepository.save(lecture);

        return lectureMapper.toResponse(lecture);
    }


    @Transactional
    public void deleteLecture(Long lectureId, Long courseId) {
        Instructor instructor = courseValidationService.validateInstructor();
        courseValidationService.validateCourseInstructor(courseId, instructor);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        fileService.deleteFromS3(lecture.getLectureUrl());

        lectureRepository.delete(lecture);

        log.info("강의 삭제 완료: {} \n", lectureId);
    }


    @Transactional(readOnly = true)
    public LectureCreateResponseDto getLectureById(Long courseId, Long lectureId) {
        Course course = courseValidationService.validateCourse(courseId);

        Lecture lecture = lectureRepository.findById(lectureId)
                .filter(l -> l.getCourse().equals(course))
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        return lectureMapper.toResponse(lecture);
    }

    @Transactional(readOnly = true)
    public List<LectureCreateResponseDto> getAllLectures(Long courseId) {
        Course course = courseValidationService.validateCourse(courseId);

        List<Lecture> lectures = lectureRepository.findAllByCourse(course);
        return lectures.stream()
                .map(lectureMapper::toResponse)
                .collect(Collectors.toList());
    }
}