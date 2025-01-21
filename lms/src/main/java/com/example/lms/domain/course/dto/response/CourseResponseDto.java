package com.example.lms.domain.course.dto.response;

import com.example.lms.domain.instructor.dto.InstructorInfo;
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
    private Long id;
    private String courseTitle;
    private String courseDescription;
    private InstructorInfo instructorInfo;
    private Integer courseStudents;
    private Integer courseCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
}