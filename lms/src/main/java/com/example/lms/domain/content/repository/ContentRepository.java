package com.example.lms.domain.content.repository;

import com.example.lms.domain.content.entity.Content;
import com.example.lms.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByCourse(Course course);
}
