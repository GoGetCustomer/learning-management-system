package com.example.lms.domain.instructor.repository;

import com.example.lms.domain.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
