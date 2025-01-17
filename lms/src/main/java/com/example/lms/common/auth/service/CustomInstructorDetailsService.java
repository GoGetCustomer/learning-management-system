package com.example.lms.common.auth.service;

import com.example.lms.common.auth.dto.CustomUserDetails;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomInstructorDetailsService implements UserDetailsService {

    private final InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return instructorRepository.findByLoginId(loginId)
                .map(instructor -> new CustomUserDetails(instructor, instructor.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("강사 정보를 찾을 수 없습니다. : " + loginId));
    }

    public UserDetails loadUserByInstructorId(String instructorId) throws UsernameNotFoundException {
        return instructorRepository.findById(Long.valueOf(instructorId))
                .map(instructor -> new CustomUserDetails(instructor, instructor.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("강사 정보를 찾을 수 없습니다. : " + instructorId));
    }
}
