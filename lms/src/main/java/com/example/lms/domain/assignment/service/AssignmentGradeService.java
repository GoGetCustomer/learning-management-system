package com.example.lms.domain.assignment.service;

import com.example.lms.domain.assignment.entity.Assignment;
import com.example.lms.domain.assignment.entity.AssignmentGrade;
import com.example.lms.domain.assignment.repository.AssignmentGradeRepository;
import com.example.lms.domain.assignment.repository.AssignmentRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentGradeService {

    private final AssignmentGradeRepository gradeRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    public AssignmentGrade createGrade(Long assignmentId, Long studentId, int grade, String feedback) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        AssignmentGrade assignmentGrade = AssignmentGrade.of(assignment, student, grade, feedback);
        return gradeRepository.save(assignmentGrade);
    }

    public List<AssignmentGrade> getGradesByAssignment(Long assignmentId) {
        return gradeRepository.findByAssignmentId(assignmentId);
    }

    public List<AssignmentGrade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
}
