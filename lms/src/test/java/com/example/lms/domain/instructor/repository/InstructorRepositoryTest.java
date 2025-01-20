package com.example.lms.domain.instructor.repository;

import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.instructor.entity.Instructor;
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
class InstructorRepositoryTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("loginId로 강사 조회 테스트")
    void findInstructorByLoginId() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        instructorRepository.save(instructor);

        // when
        Instructor saveInstructor = instructorRepository.findByLoginIdAndNotDeleted(instructor.getLoginId())
                .orElseThrow(() -> new IllegalStateException("강사를 찾을 수 없습니다."));

        // then
        assertThat(saveInstructor.getLoginId()).isEqualTo(instructor.getLoginId());
    }

    @Test
    @DisplayName("회원 탈퇴한 강사 조회 테스트")
    void findDeletedInstructorByLoginId() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        Long id = instructorRepository.save(instructor).getId();
        instructorRepository.updateIsDeleted(id);

        // when
        Optional<Instructor> saveInstructor = instructorRepository.findByLoginIdAndNotDeleted(instructor.getLoginId());

        // then
        assertThat(saveInstructor).isEmpty();
    }

    @Test
    @DisplayName("학생 isDeleted 업데이트")
    void updateIsDeleted() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        instructorRepository.save(instructor);

        // when
        instructorRepository.updateIsDeleted(instructor.getId());

        // then
        Instructor deletedInstructor = instructorRepository.findById(instructor.getId())
                .orElseThrow(() -> new IllegalStateException("학생을 찾을 수 없습니다."));

        assertThat(deletedInstructor.getIsDeleted()).isTrue();
    }
}