package com.example.lms.domain.student.service;

import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(StudentCreateRequestDto studentCreateRequestDto) {

        checkPassword(studentCreateRequestDto.getPassword(), studentCreateRequestDto.getPasswordCheck());
        checkLoginIdDuplicate(studentCreateRequestDto.getLoginId());
        checkEmailDuplicate(studentCreateRequestDto.getEmail());

        return studentRepository.save(Student.of(
                studentCreateRequestDto.getLoginId(),
                bCryptPasswordEncoder.encode(studentCreateRequestDto.getPassword()),
                studentCreateRequestDto.getEmail(),
                studentCreateRequestDto.getName())).getId();
    }

    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }
    }

    public void checkLoginIdDuplicate(String loginId) {
        if (studentRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("아이디 중복");
        }
    }

    public void checkEmailDuplicate(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이메일 중복");
        }
    }
}
