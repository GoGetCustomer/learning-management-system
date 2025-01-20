package com.example.lms.domain.course.repository;

import com.example.lms.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, Long> {
}
