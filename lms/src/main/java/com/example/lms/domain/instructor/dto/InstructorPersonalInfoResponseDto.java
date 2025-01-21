package com.example.lms.domain.instructor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "InstructorPersonalInfoResponseDto: 강사 정보 응답 Dto")
public class InstructorPersonalInfoResponseDto {

    @Schema(description = "강사 고유 식별자", example = "1")
    private final Long id;

    @Schema(description = "강사 아이디", example = "helloworld")
    private final String loginId;

    @Schema(description = "강사명", example = "김강사")
    private final String name;

    @Schema(description = "강사 소개", example = "총 경력 8년. 수강생 만족도 1등. 환영합니다.")
    private final String description;

    @Schema(description = "강사 이메일(연락처)", example = "goget@gmail.com")
    private final String email;

    @Builder
    public InstructorPersonalInfoResponseDto(Long id, String loginId, String name, String description, String email) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.description = description;
        this.email = email;
    }
}
