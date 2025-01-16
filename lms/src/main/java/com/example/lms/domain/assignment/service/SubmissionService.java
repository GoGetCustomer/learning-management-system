package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.dto.SubmissionDto;
import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.entity.Submission;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.assignment.repository.SubmissionRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    public Submission submitAssignment(Long assignmentId, SubmissionDto dto) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(dto.getFileUrl());
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }
}
