package com.example.lms.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {
    private Long courseId;
    private String courseTitle;
    private String instructorName;
    private int courseStudents;
    private String startDate;
    private String endDate;
}