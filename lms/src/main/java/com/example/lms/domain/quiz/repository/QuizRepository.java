package com.example.lms.domain.quiz.repository;

import com.example.lms.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q WHERE q.courseId = :courseId")
    List<Quiz> findByCourseId(@Param("courseId") Long courseId);
}