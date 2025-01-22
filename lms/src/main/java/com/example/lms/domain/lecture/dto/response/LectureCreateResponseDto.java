package com.example.lms.domain.lecture.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureCreateResponseDto {
    private Long id;
    private String lectureTitle;
    private String lectureDescription;
    private String lectureUrl;
    private Long courseId;
    private LocalTime lectureTime; // 임의 설정 예정.
}
