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
@Schema(name = "CourseCreateRequestDto : 과정 생성 요청 Dto")
public class CourseCreateRequestDto {

    @NotBlank(message = "과정 제목은 필수 입력값입니다.")
    @Size(min = 1, max = 50, message = "과정 제목은 1자 이상 50자 이하로 입력해주세요.")
    @Schema(description = "과정 제목은 1자 이상 50자 이하로 입력해주세요.", example = "스프링 백엔드 기초 과정")
    private String courseTitle;

    @NotBlank(message = "과정 설명은 필수 입력값입니다.")
    @Schema(description = "과정에 대한 설명을 입력해주세요.", example = "스프링 백엔드 기초에 대해 가볍게 살펴보는 과정입니다. 초보자들에게 적합한 과정입니다.")
    private String courseDescription;

    @NotNull(message = "과정 시작일은 필수 입력값입니다.")
    @Schema(description = "과정이 시작되는 날짜를 입력해주세요.", example = "2025-03-02")
    private LocalDate startDate;

    @NotNull(message = "과정 종료일은 필수 입력값입니다.")
    @Schema(description = "과정이 종료되는 날짜를 입력해주세요.", example = "2025-04-02")
    private LocalDate endDate;

    @Min(value = 0, message = "수강 정원은 0명 이상이어야 합니다.")
    @Schema(description = "과정 최대 수강 가능 인원을 입력해주세요. 수강 정원은 0명 이상이어야 합니다.", example = "50")
    private Integer courseCapacity;
}
