package com.example.lms.domain.assignment.controller;

import com.example.lms.domain.assignment.dto.AssignmentGradeRequest;
import com.example.lms.domain.assignment.dto.AssignmentGradeResponse;
import com.example.lms.domain.assignment.entity.AssignmentGrade;
import com.example.lms.domain.assignment.service.AssignmentGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/assignments/{assignmentId}/grades")
@RequiredArgsConstructor
public class AssignmentGradeController {

    private final AssignmentGradeService gradeService;

    // 성적 등록
    @PostMapping
    public ResponseEntity<AssignmentGradeResponse> createGrade(
            @PathVariable Long assignmentId,
            @RequestBody AssignmentGradeRequest request) {
        AssignmentGrade grade = gradeService.createGrade(
                assignmentId, request.getStudentId(), request.getGrade(), request.getFeedback());

        return ResponseEntity.ok(new AssignmentGradeResponse(grade));
    }

    // 과제별 성적 조회
    @GetMapping
    public ResponseEntity<List<AssignmentGradeResponse>> getGradesByAssignment(@PathVariable Long assignmentId) {
        List<AssignmentGrade> grades = gradeService.getGradesByAssignment(assignmentId);
        List<AssignmentGradeResponse> response = grades.stream()
                .map(AssignmentGradeResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
