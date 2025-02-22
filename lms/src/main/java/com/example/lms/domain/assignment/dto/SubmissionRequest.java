package com.example.lms.domain.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubmissionRequest {
    private Long studentId;
    private String fileUrl;
}
