package com.example.lms.domain.student.service;

import com.example.lms.domain.student.dto.StudentBasicInfoResponseDto;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.dto.StudentPersonalInfoResponseDto;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.teaching.repository.TeachingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TeachingRepository teachingRepository;

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

    public StudentPersonalInfoResponseDto findPersonalInfo() {
        Student student = studentRepository.findById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));
        return StudentPersonalInfoResponseDto.builder()
                .id(student.getId())
                .loginId(student.getLoginId())
                .name(student.getName())
                .email(student.getEmail())
                .build();
    }

    public StudentBasicInfoResponseDto findBasicInfoForInstructor(Long studentId, Long courseId) {
        Long instructorId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!isAuthorizedInstructorForCourse(instructorId, courseId)) {
            throw new IllegalArgumentException("과정 진행 강사만 수강 학생 정보를 조회할 수 있습니다.");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
        return StudentBasicInfoResponseDto.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .build();
    }

    private boolean isAuthorizedInstructorForCourse(Long instructorId, Long courseId) {
        return teachingRepository.existsByCourseIdIdAndInstructorId(courseId, instructorId);
    }
}
