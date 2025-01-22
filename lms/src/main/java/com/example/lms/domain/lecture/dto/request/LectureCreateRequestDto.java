package com.example.lms.domain.lecture.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureCreateRequestDto {
    @NotBlank(message = "강의 제목은 필수 입력 값입니다.")
    @Schema(description = "업로드하는 강의 제목을 입력해주세요.", example = "스프링 부트 강의 part1")
    private String lectureTitle;

    @NotBlank(message = "강의 설명은 필수 입력 값입니다.")
    @Schema(description = "업로드하는 강의 설명을 입력해주세요.", example = "스프링 부트 과정에서 첫걸음을 내딛는 강의입니다! 편안하게 들어주세요.")
    private String lectureDescription;

}
