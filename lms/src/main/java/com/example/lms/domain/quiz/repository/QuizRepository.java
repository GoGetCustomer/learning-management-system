package com.example.lms.domain.quiz.repository;

import com.example.lms.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllByCourseId(Long courseId);
}