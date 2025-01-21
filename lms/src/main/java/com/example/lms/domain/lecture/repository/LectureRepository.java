package com.example.lms.domain.lecture.repository;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findAllByCourse(Course course);
}
