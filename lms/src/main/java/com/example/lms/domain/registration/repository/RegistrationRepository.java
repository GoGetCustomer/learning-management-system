package com.example.lms.domain.registration.repository;

import com.example.lms.domain.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
