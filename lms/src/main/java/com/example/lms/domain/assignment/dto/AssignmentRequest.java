package com.example.lms.domain.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
}
