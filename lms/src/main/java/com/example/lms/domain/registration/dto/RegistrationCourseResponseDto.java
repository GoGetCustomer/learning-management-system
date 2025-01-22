package com.example.lms.domain.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "RegistrationCourseResponseDto: 과정에 수강 신청한 학생 페이지 응답 Dto")
public class RegistrationCourseResponseDto {

    @Schema(description = "수강 신청 식별자", example = "1")
    private final Long id;

    @Schema(description = "수강 신청 상태", example = "APPROVED")
    private final String registrationStatus;

    @Schema(description = "수강 신청일")
    private final LocalDateTime createAt;

    @Schema(description = "학생 식별자", example = "1")
    private final Long studentId;

    @Schema(description = "학생 이름", example = "김학생")
    private final String studentName;

    @Schema(description = "학생 이메일", example = "hello@gmail.com")
    private final String studentEmail;

    @Schema(description = "과정 식별자", example = "2")
    private final Long courseId;

    @Schema(description = "과정 이름", example = "스프링 백엔드 기초 과정")
    private final String courseTitle;

    @Builder
    public RegistrationCourseResponseDto(Long id, String registrationStatus, LocalDateTime createAt, Long studentId, String studentName, String studentEmail, Long courseId, String courseTitle) {
        this.id = id;
        this.registrationStatus = registrationStatus;
        this.createAt = createAt;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }
}
