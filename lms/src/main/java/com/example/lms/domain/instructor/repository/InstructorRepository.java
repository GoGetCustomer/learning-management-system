package com.example.lms.domain.instructor.repository;

import com.example.lms.domain.instructor.entity.Instructor;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);

    @Query("select i from Instructor i where i.loginId = :loginId and i.isDeleted = false")
    Optional<Instructor> findByLoginIdAndNotDeleted(@Param("loginId") String loginId);

    @Query("select i from Instructor i where i.id = :id and i.isDeleted = false")
    Optional<Instructor> findByIdAndNotDeleted(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Instructor i set i.isDeleted = true where i.id = :id")
    void updateIsDeleted(@Param("id") Long id);
}
