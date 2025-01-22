package com.example.lms.domain.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "RegistrationStudentResponseDto: 학생 수강 신청 히스토리 응답 Dto")
public class RegistrationStudentResponseDto {
    @Schema(description = "수강 신청 식별자", example = "1")
    private final Long id;

    @Schema(description = "수강 신청 상태", example = "CANCEL")
    private final String registrationStatus;

    @Schema(description = "수강 신청일")
    private final LocalDateTime createAt;

    @Schema(description = "과정 식별자", example = "2")
    private final Long courseId;

    @Schema(description = "과정 이름", example = "스프링 백엔드 기초 과정")
    private final String courseTitle;

    @Schema(description = "과정 설명", example = "스프링 백엔드 기초에 대해 가볍게 살펴보는 과정입니다. 초보자들에게 적합한 과정입니다.")
    private final String courseDescription;

    @Builder
    public RegistrationStudentResponseDto(Long id, String registrationStatus, LocalDateTime createAt, Long courseId, String courseTitle, String courseDescription) {
        this.id = id;
        this.registrationStatus = registrationStatus;
        this.createAt = createAt;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
    }
}
