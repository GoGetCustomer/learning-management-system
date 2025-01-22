package com.example.lms.domain.registration.repository;

import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.enums.RegistrationStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Query("select count(r) > 0 from Registration r where r.course.id = :courseId and r.student.id = :studentId")
    boolean existsByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query("select count(r) > 0 from Registration r where r.id = :registrationId and r.registrationStatus =:status")
    boolean existsByRegistrationIdAndStatus(@Param("registrationId") Long registrationId, @Param("status") RegistrationStatus status);

    @Query("select r from Registration r left join fetch r.course left join fetch r.student where r.student.id =:studentId")
    Page<Registration> findPageByStudentIdWithCourse(@Param("studentId") Long studentId, Pageable pageable);
}
