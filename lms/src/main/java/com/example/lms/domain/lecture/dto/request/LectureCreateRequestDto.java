package com.example.lms.domain.lecture.dto.request;

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
    private String lectureTitle;

    @NotBlank(message = "강의 설명은 필수 입력 값입니다.")
    private String lectureDescription;

}
