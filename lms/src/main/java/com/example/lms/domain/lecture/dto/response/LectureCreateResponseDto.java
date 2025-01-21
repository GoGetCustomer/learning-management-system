package com.example.lms.domain.lecture.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureCreateResponseDto {
    private String lectureTitle;
    private String lectureDescription;
    private String lectureUrl; // https://{bucket}.s3.amazonaws.com/{key}
    private Long courseId;
    private LocalTime lectureTime;
}
