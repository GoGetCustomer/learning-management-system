package com.example.lms.domain.assignment.controller;

import com.example.lms.domain.assignment.dto.AssignmentRequest;
import com.example.lms.domain.assignment.dto.AssignmentResponse;
import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(
            @PathVariable Long courseId,
            @RequestBody AssignmentRequest assignmentRequest) {
        Assignment assignment = assignmentService.createAssignment(courseId, assignmentRequest);
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());
        response.setTitle(assignment.getTitle());
        response.setDescription(assignment.getDescription());
        response.setDueDate(assignment.getDueDate());
        return ResponseEntity.ok(response);
    }
}