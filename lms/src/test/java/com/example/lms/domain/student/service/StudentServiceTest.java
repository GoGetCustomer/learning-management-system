package com.example.lms.domain.student.service;

import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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

}