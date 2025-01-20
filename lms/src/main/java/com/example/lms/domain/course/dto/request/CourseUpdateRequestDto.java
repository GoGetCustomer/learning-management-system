package com.example.lms.domain.course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequestDto {
    @Schema(description = "과정 제목", example = "Java Advanced Course")
    @NotBlank(message = "과정 제목은 필수 입력값입니다.")
    @Size(min = 1, max = 50, message = "과정 제목은 1자 이상 50자 이하로 입력해주세요.")
    private String courseTitle;

    @Schema(description = "과정 설명", example = "Java의 고급 주제를 학습하는 과정입니다.")
    @NotBlank(message = "과정 설명은 필수 입력값입니다.")
    private String courseDescription;


    @Schema(description = "과정 시작일", example = "2025-01-23")
    @NotNull(message = "과정 시작일은 필수 입력값입니다.")
    private LocalDate startDate;

    @Schema(description = "과정 종료일", example = "2025-02-23")
    @NotNull(message = "과정 종료일은 필수 입력값입니다.")
    private LocalDate endDate;

    @Schema(description = "수강 정원", example = "30")
    @Min(value = 0, message = "수강 정원은 0명 이상이어야 합니다.")
    private Integer courseCapacity;
}