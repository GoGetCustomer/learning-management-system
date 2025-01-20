package com.example.lms.domain.course.repository;

import com.example.lms.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.teachings t WHERE t.instructor.id = :instructorId")
    List<Course> findAllByInstructorId(Long instructorId);
}
