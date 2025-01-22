package com.example.lms.domain.lecture.service;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.entity.Lecture;
import com.example.lms.domain.lecture.mapper.LectureMapper;
import com.example.lms.domain.lecture.repository.LectureRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {
    private final S3Operations s3Operations;
    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;
    private final LectureMapper lectureMapper;
    private final InstructorRepository instructorRepository;

    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    @Value("${cloud.aws.region.static}")
    String region;

    @Transactional
    public LectureCreateResponseDto createLecture(LectureCreateRequestDto request,
                                                  Long courseId, MultipartFile multipartFile) throws IOException {

        // 현재 인증된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        // 강사 검증
        Instructor instructor = instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));

        // 강좌 검증
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 강좌에 강의를 추가할 수 없습니다.");
        }

        // S3에 파일 업로드
        String fileName = generateUniqueFileName(multipartFile.getOriginalFilename());
        uploadToS3(multipartFile, fileName);

        // S3 URL 생성
        String fileUrl = generateS3FileUrl(fileName);

        // 강의 시간 설정 (예제 값 사용)
        LocalTime lectureTime = LocalTime.of(0, 10);

        // 강의 엔티티 생성
        Lecture lecture = lectureMapper.toEntity(request, course, fileUrl, lectureTime);

        // 강의 저장
        lectureRepository.save(lecture);

        // 응답 DTO 반환
        return lectureMapper.toResponse(lecture);
    }

    private void uploadToS3(MultipartFile multipartFile, String fileName) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Operations.upload(bucket, fileName, inputStream,
                    ObjectMetadata.builder()
                            .contentType(multipartFile.getContentType())
                            .build());
        }
        log.info("S3에 파일 업로드 완료: {} \n", fileName);
    }

    private String generateUniqueFileName(String originalFileName) {
        String sanitizedFileName = originalFileName.replace(" ", "_");
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + sanitizedFileName;
    }

    private String generateS3FileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    @Transactional
    public void deleteLecture(Long lectureId, Long courseId) {

        // 현재 인증된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf(authentication.getName());

        // 강사 검증
        Instructor instructor = instructorRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 강사가 아닙니다."));
        // 과정 조회
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 과정이 존재하지 않습니다."));

        if (!course.belongsToInstructor(instructor)) {
            throw new IllegalArgumentException("해당 과정에 강의를 추가할 수 없습니다.");
        }

        // 강의 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        // 강의 파일 S3에서 삭제
        deleteFromS3(lecture.getLectureUrl());

        // 데이터베이스에서 강의 삭제
        lectureRepository.delete(lecture);

        log.info("강의 삭제 완료: {} \n", lectureId);
    }

    private void deleteFromS3(String lectureUrl) {
        // URL에서 파일 이름 추출
        String fileName = lectureUrl.substring(lectureUrl.lastIndexOf("/") + 1);

        // S3에서 파일 삭제 요청
        s3Operations.deleteObject(bucket, fileName);

        log.info("S3에서 파일 삭제 완료: {} \n", fileName);
    }

    @Transactional(readOnly = true)
    public LectureCreateResponseDto getLectureById(Long courseId, Long lectureId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 과정이 존재하지 않습니다."));

        Lecture lecture = lectureRepository.findById(lectureId)
                .filter(l -> l.getCourse().equals(course))
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        return lectureMapper.toResponse(lecture);
    }

    @Transactional(readOnly = true)
    public List<LectureCreateResponseDto> getAllLectures(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 과정이 존재하지 않습니다."));

        List<Lecture> lectures = lectureRepository.findAllByCourse(course);
        return lectures.stream()
                .map(lectureMapper::toResponse)
                .collect(Collectors.toList());
    }
}