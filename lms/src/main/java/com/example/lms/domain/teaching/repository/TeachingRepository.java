package com.example.lms.domain.teaching.repository;

import com.example.lms.domain.teaching.entity.Teaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeachingRepository extends JpaRepository<Teaching, Long> {
    @Query("select t from Teaching t left join fetch t.course left join fetch t.instructor where t.instructor.id =:id")
    Page<Teaching> findPageByInstructorId(@Param("id") Long id, Pageable pageable);

    @Query("select count(t) > 0 from Teaching t where t.instructor.id = :id")
    boolean existsByInstructorId(@Param("id") Long id);
}
