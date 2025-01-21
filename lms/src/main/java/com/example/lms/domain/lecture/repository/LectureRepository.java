package com.example.lms.domain.lecture.repository;

import com.example.lms.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
