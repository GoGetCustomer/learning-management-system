package com.example.lms.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateResponseDto {
    private Long id;
    private String courseTitle;
    private String courseDescription;
    private Integer courseCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
}
