package com.example.lms.domain.student.repository;

import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.student.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("loginId로 강사 조회 테스트")
    void findInstructorByLoginId() throws Exception {
        // given
        Student student = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        studentRepository.save(student);

        // when
        Student saveStudent = studentRepository.findByLoginId(student.getLoginId())
                .orElseThrow(() -> new Exception("찾을 수 없습니다."));

        // then
        assertThat(saveStudent.getLoginId()).isEqualTo(student.getLoginId());
    }
}