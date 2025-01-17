package com.example.lms.domain.instructor.repository;

import com.example.lms.domain.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}
