package com.example.lms.domain.assignment.controller;

import com.example.lms.domain.assignment.dto.AssignmentDto;
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
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long courseId,
            @RequestBody AssignmentDto assignmentDto) {
        Assignment assignment = assignmentService.createAssignment(courseId, assignmentDto);
        return ResponseEntity.ok(assignment);
    }
}
