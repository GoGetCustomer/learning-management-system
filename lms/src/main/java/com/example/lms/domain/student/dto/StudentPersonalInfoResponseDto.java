package com.example.lms.domain.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "StudentPersonalInfoResponseDto: 학생 개인정보 응답 Dto")
public class StudentPersonalInfoResponseDto {
    @Schema(description = "학생 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "학생 아이디", example = "helloworld")
    private final String loginId;

    @Schema(description = "학생 이름", example = "김학생")
    private final String name;

    @Schema(description = "학생 이메일(연락처)", example = "hello@gmail.com")
    private final String email;

    @Builder
    public StudentPersonalInfoResponseDto(Long id, String loginId, String name, String email) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
    }
}
