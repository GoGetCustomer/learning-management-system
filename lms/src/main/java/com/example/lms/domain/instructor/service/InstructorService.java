package com.example.lms.domain.instructor.service;

import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(InstructorCreateRequestDto instructorCreateRequestDto) {
        checkPassword(instructorCreateRequestDto.getPassword(), instructorCreateRequestDto.getPasswordCheck());
        checkLoginIdDuplicate(instructorCreateRequestDto.getLoginId());
        checkEmailDuplicate(instructorCreateRequestDto.getEmail());

        return instructorRepository.save(Instructor.of(
                instructorCreateRequestDto.getLoginId(),
                bCryptPasswordEncoder.encode(instructorCreateRequestDto.getPassword()),
                instructorCreateRequestDto.getEmail(),
                instructorCreateRequestDto.getName(),
                instructorCreateRequestDto.getDescription())).getId();
    }

    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }
    }

    private void checkLoginIdDuplicate(String loginId) {
        if (instructorRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("아이디 중복");
        }
    }

    private void checkEmailDuplicate(String email) {
        if (instructorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이메일 중복");
        }
    }
}
