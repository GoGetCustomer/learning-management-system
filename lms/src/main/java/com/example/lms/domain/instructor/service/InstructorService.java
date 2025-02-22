package com.example.lms.domain.instructor.service;

import com.example.lms.domain.instructor.dto.InstructorBasicInfoResponseDto;
import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.dto.InstructorPersonalInfoResponseDto;
import com.example.lms.domain.instructor.dto.InstructorUpdateRequestDto;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public InstructorPersonalInfoResponseDto findPersonalInfo() {
        Instructor instructor = instructorRepository.findById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new IllegalArgumentException("강사 정보를 찾지 못했습니다."));
        return InstructorPersonalInfoResponseDto.builder()
                .id(instructor.getId())
                .loginId(instructor.getLoginId())
                .name(instructor.getName())
                .description(instructor.getDescription())
                .email(instructor.getEmail())
                .build();
    }

    public InstructorBasicInfoResponseDto findBasicInfo(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("강사 정보를 찾지 못했습니다."));
        return InstructorBasicInfoResponseDto.builder()
                .name(instructor.getName())
                .description(instructor.getDescription())
                .email(instructor.getEmail())
                .build();
    }

    @Transactional
    public Long update(InstructorUpdateRequestDto instructorUpdateRequestDto) {
        checkEmailDuplicate(instructorUpdateRequestDto.getEmail());
        Instructor instructor = instructorRepository.findById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new IllegalArgumentException("강사 정보를 찾지 못했습니다."));
        instructor.update(
                instructorUpdateRequestDto.getName(),
                instructorUpdateRequestDto.getDescription(),
                instructorUpdateRequestDto.getEmail());
        return instructor.getId();
    }

    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }
    }

    public void checkLoginIdDuplicate(String loginId) {
        if (instructorRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("아이디 중복");
        }
    }

    public void checkEmailDuplicate(String email) {
        if (instructorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이메일 중복");
        }
    }
}
