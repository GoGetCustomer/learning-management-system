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
public class CourseResponseDto {
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private String instructorName;
    private Integer courseStudents;
    private LocalDate startDate;
    private LocalDate endDate;
}