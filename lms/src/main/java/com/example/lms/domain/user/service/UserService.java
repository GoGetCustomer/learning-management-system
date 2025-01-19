package com.example.lms.domain.user.service;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.AUTHORITIES_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void delete(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        Claims claimsByAccessToken = tokenProvider.getClaimsByAccessToken(accessToken);

        String id = claimsByAccessToken.getSubject();
        String role = claimsByAccessToken.get(AUTHORITIES_KEY).toString();

        tokenProvider.invalidateRefreshToken(role, id);

        if (role.equals(Role.STUDENT.getAuthority())) {
            studentRepository.updateIsDeleted(Long.valueOf(id));
        }
        if (role.equals(Role.INSTRUCTOR.getAuthority())) {
            instructorRepository.updateIsDeleted(Long.valueOf(id));
        }
        log.info("{}-{}: delete ({})", id, role, new Date());
    }
}
