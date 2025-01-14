package com.example.lms.domain.student.entity;

import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.lms.common.fixture.StudentFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("학생 엔티티 저장 테스트")
    void studentEntitySaveAndRetrieveTest() {
        //given
        Student student = STUDENT_FIXTURE_1.createStudent();

        //when
        studentRepository.save(student);

        //then
        Student savedStudent = studentRepository.findById(student.getId()).orElseThrow();

        assertAll(
                () -> assertThat(savedStudent.getId()).isInstanceOf(Long.class),
                () -> assertThat(savedStudent.getLoginId()).isEqualTo(student.getLoginId()),
                () -> assertThat(savedStudent.getEmail()).isEqualTo(student.getEmail()),
                () -> assertThat(savedStudent.getPassword()).isEqualTo(student.getPassword()),
                () -> assertThat(savedStudent.getName()).isEqualTo(student.getName()),
                () -> assertThat(savedStudent.getRole()).isEqualTo(Role.STUDENT)
        );
    }
}