package com.example.lms.domain.user.service;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.dto.UserUpdatePasswordRequestDto;
import com.example.lms.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Transactional
    public void updatePassword(UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String password = userUpdatePasswordRequestDto.getPassword();
        String newPassword = userUpdatePasswordRequestDto.getNewPassword();
        String newPasswordCheck = userUpdatePasswordRequestDto.getNewPasswordCheck();

        checkNewPassword(newPassword, newPasswordCheck);

        if (role.equals(Role.STUDENT.getAuthority())) {
            updateStudentPassword(id, password, newPassword);
            tokenProvider.invalidateRefreshToken(role, id);
        }
        if (role.equals(Role.INSTRUCTOR.getAuthority())) {
            updateInstructorPassword(id, password, newPassword);
            tokenProvider.invalidateRefreshToken(role, id);
        }
    }

    private void updateStudentPassword(String id, String password, String newPassword) {
        Student student = studentRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
        checkPassword(password, student.getPassword());
        student.updatePassword(bCryptPasswordEncoder.encode(newPassword));
    }

    private void updateInstructorPassword(String id, String password, String newPassword) {
        Instructor instructor = instructorRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("강사을 찾을 수 없습니다."));
        checkPassword(password, instructor.getPassword());
        instructor.updatePassword(bCryptPasswordEncoder.encode(newPassword));
    }

    private void checkPassword(String password, String savedPassword) {
        if (!bCryptPasswordEncoder.matches(password, savedPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkNewPassword(String newPassword, String newPasswordCheck) {
        if (!newPassword.equals(newPasswordCheck)) {
            throw new IllegalArgumentException("새 비밀번호 확인이 일치하지 않습니다.");
        }
    }
}
