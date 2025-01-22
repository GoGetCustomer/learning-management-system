package com.example.lms.domain.content.repository;

import com.example.lms.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
