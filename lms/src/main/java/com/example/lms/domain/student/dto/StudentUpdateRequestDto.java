package com.example.lms.domain.student.dto;

import com.example.lms.common.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "StudentUpdateRequestDto: 학생 정보 수정 요청 Dto")
public class StudentUpdateRequestDto {

    @NotBlank(message = "이름이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "학생 이름입니다.",
            example = "김수료")
    private String name;

    @NotBlank(message = "이메일이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "이메일 형식에 맞지 않습니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "이메일 형식이여야 합니다.",
            example = "hihi@gmail.com")
    private String email;

    @Builder
    public StudentUpdateRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
