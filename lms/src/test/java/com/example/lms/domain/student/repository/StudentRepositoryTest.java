package com.example.lms.domain.student.repository;

import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.student.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("loginId로 학생 조회 테스트")
    void findStudentByLoginId() {
        // given
        Student student = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        studentRepository.save(student);

        // when
        Student saveStudent = studentRepository.findByLoginIdAndNotDeleted(student.getLoginId())
                .orElseThrow(() -> new IllegalStateException("학생을 찾을 수 없습니다."));

        // then
        assertThat(saveStudent.getLoginId()).isEqualTo(student.getLoginId());
    }

    @Test
    @DisplayName("회원 탈퇴한 학생 조회 테스트")
    void findDeletedStudentByLoginId() {

        // given
        Student student = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        Long id = studentRepository.save(student).getId();
        studentRepository.updateIsDeleted(id);

        // when
        Optional<Student> saveStudent = studentRepository.findByLoginIdAndNotDeleted(student.getLoginId());

        // then
        assertThat(saveStudent).isEmpty();
    }

    @Test
    @DisplayName("학생 isDeleted 업데이트")
    void updateIsDeleted() {
        // given
        Student student = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        studentRepository.save(student);

        // when
        studentRepository.updateIsDeleted(student.getId());

        // then
        Student deletedStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalStateException("학생을 찾을 수 없습니다."));

        assertThat(deletedStudent.getIsDeleted()).isTrue();
    }
}