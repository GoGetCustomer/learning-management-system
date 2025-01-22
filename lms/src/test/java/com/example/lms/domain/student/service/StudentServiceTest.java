package com.example.lms.domain.student.service;

import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.dto.StudentPersonalInfoResponseDto;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Test
    @DisplayName("학생으로 회원가입을 한다.")
    void studentJoinTest() throws Exception {
        //given
        StudentCreateRequestDto studentCreateRequestDto = StudentFixture.STUDENT_FIXTURE_1.signUpStudent();

        //when
        Long id = studentService.join(studentCreateRequestDto);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("찾을 수 없는 학생"));

        //then
        assertAll(
                () -> assertThat(studentCreateRequestDto.getLoginId()).isEqualTo(student.getLoginId()),
                () -> assertThat(studentCreateRequestDto.getName()).isEqualTo(student.getName()),
                () -> assertThat(studentCreateRequestDto.getEmail()).isEqualTo(student.getEmail())
        );
    }

    @Test
    @DisplayName("아이디가 중복되면 에러를 반환한다.")
    void studentCheckLoginIdTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        String loginId = student.getLoginId();

        //when & then
        assertThatThrownBy(() -> studentService.checkLoginIdDuplicate(loginId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이메일이 중복되면 에러를 반환한다.")
    void studentCheckEmailTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        String email = student.getEmail();

        //when & then
        assertThatThrownBy(() -> studentService.checkEmailDuplicate(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("학생이 개인정보를 조회한다.")
    void getPersonalInfoTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(student.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))));

        //when
        StudentPersonalInfoResponseDto studentPersonalInfoResponseDto = studentService.findPersonalInfo();

        //then
        assertAll(
                () -> assertThat(studentPersonalInfoResponseDto.getId()).isEqualTo(student.getId()),
                () -> assertThat(studentPersonalInfoResponseDto.getLoginId()).isEqualTo(student.getLoginId()),
                () -> assertThat(studentPersonalInfoResponseDto.getName()).isEqualTo(student.getName()),
                () -> assertThat(studentPersonalInfoResponseDto.getEmail()).isEqualTo(student.getEmail())
        );
    }
}