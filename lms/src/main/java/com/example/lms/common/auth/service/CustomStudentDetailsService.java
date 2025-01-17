package com.example.lms.common.auth.service;

import com.example.lms.common.auth.dto.CustomUserDetails;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomStudentDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return studentRepository.findByLoginId(loginId)
                .map(student -> new CustomUserDetails(student, student.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("학생 정보를 찾을 수 없습니다. : " + loginId));
    }
}

