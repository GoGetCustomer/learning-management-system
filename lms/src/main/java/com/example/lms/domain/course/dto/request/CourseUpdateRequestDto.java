package com.example.lms.domain.course.dto.request;

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
    @NotBlank(message = "과정 제목은 필수 입력값입니다.")
    @Size(min = 1, max = 50, message = "과정 제목은 1자 이상 50자 이하로 입력해주세요.")
    private String courseTitle;

    @NotBlank(message = "과정 설명은 필수 입력값입니다.")
    private String courseDescription;

    @NotNull(message = "과정 시작일은 필수 입력값입니다.")
    private LocalDate startDate;

    @NotNull(message = "과정 종료일은 필수 입력값입니다.")
    private LocalDate endDate;

    @Min(value = 0, message = "수강 정원은 0명 이상이어야 합니다.")
    private Integer courseCapacity;
}