package com.example.lms.domain.assignment.controller;

import com.example.lms.domain.assignment.dto.SubmissionDto;
import com.example.lms.domain.assignment.entity.Submission;
import com.example.lms.domain.assignment.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments/{assignmentId}/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody SubmissionDto submissionDto) {
        Submission submission = submissionService.submitAssignment(assignmentId, submissionDto);
        return ResponseEntity.ok(submission);
    }
}
