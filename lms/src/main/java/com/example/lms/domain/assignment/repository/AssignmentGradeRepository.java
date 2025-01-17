package com.example.lms.domain.assignment.repository;

import com.example.lms.domain.assignment.entity.AssignmentGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentGradeRepository extends JpaRepository<AssignmentGrade, Long> {
    List<AssignmentGrade> findByAssignmentId(Long assignmentId);
    List<AssignmentGrade> findByStudentId(Long studentId);
}
