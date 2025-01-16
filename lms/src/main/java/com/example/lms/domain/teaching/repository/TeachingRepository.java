package com.example.lms.domain.teaching.repository;

import com.example.lms.domain.teaching.entity.Teaching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingRepository extends JpaRepository<Teaching, Long> {
}
