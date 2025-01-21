package com.example.lms.domain.instructor.dto;

import com.example.lms.common.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InstructorUpdateRequestDto {

    @NotBlank(message = "이름이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "사용자 이름입니다.",
            example = "김일타")
    private String name;

    @NotBlank(message = "이메일이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "이메일 형식에 맞지 않습니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "이메일 형식이여야 합니다.",
            example = "hello@gmail.com")
    private String email;

    @NotBlank(message = "강사 설명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "강사 설명 입니다.",
            example = "안녕하세요? 현재 전국일주 중이라 3개월 후 개강합니다.")
    private String description;

    @Builder
    public InstructorUpdateRequestDto(String name, String email, String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }
}
