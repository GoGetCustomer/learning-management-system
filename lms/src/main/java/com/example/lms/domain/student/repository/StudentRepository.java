package com.example.lms.domain.student.repository;

import com.example.lms.domain.student.entity.Student;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);

    @Query("select s from Student s where s.loginId = :loginId and s.isDeleted = false")
    Optional<Student> findByLoginIdAndNotDeleted(String loginId);

    @Query("select s from Student s where s.id = :id and s.isDeleted = false")
    Optional<Student> findByIdAndNotDeleted(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Student s set s.isDeleted = true where s.id = :id")
    void updateIsDeleted(@Param("id") Long id);
}
