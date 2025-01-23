package com.example.lms.domain.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "StudentBasicInfoResponseDto: 학생 기본정보 응답 Dto")
public class StudentBasicInfoResponseDto {

    @Schema(description = "학생 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "학생 이름", example = "김학생")
    private final String name;

    @Schema(description = "학생 이메일(연락처)", example = "hello@gmail.com")
    private final String email;

    @Builder
    public StudentBasicInfoResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
