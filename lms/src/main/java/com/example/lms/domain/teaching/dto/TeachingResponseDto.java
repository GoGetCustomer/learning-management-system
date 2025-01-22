package com.example.lms.domain.teaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(name = "TeachingResponseDto: 수업 정보 응답 Dto")
public class TeachingResponseDto {
    @Schema(description = "수업 식별자", example = "1")
    private final Long id;

    @Schema(description = "수업 진행 강사명", example = "김강사")
    private final String instructorName;

    @Schema(description = "수업 진행 강사 이메일", example = "hello@gmail.com")
    private final String instructorEmail;

    @Schema(description = "과정 식별자", example = "2")
    private final Long courseId;

    @Schema(description = "과정 이름", example = "스프링 백엔드 기초 과정")
    private final String courseTitle;

    @Schema(description = "과정 설명", example = "스프링 백엔드 기초에 대해 가볍게 살펴보는 과정입니다. 초보자들에게 적합한 과정입니다.")
    private final String courseDescription;

    @Schema(description = "과정 시작 일")
    private final LocalDate startDate;

    @Schema(description = "과정 종료 일")
    private final LocalDate endDate;

    @Builder
    public TeachingResponseDto(Long id, String instructorName, String instructorEmail, Long courseId, String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
