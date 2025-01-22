package com.example.lms.domain.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponseDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long courseId;
}
