package com.example.lms.domain.assignment.controller;

import com.example.lms.domain.assignment.dto.SubmissionRequest;
import com.example.lms.domain.assignment.dto.SubmissionResponse;
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
    public ResponseEntity<SubmissionResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody SubmissionRequest submissionRequest) {
        Submission submission = submissionService.submitAssignment(assignmentId, submissionRequest);

        SubmissionResponse response = new SubmissionResponse();
        response.setId(submission.getId());
        response.setAssignmentId(submission.getAssignment().getId());
        response.setStudentId(submission.getStudent().getId());
        response.setFileUrl(submission.getFileUrl());
        response.setSubmittedAt(submission.getSubmittedAt());

        return ResponseEntity.ok(response);
    }
}