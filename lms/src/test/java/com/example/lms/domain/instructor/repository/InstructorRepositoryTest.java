package com.example.lms.domain.instructor.repository;

import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.instructor.entity.Instructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class InstructorRepositoryTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("loginId로 강사 조회 테스트")
    void findInstructorByLoginId() throws Exception {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        instructorRepository.save(instructor);

        // when
        Instructor saveInstructor = instructorRepository.findByLoginId(instructor.getLoginId())
                .orElseThrow(() -> new Exception("찾을 수 없습니다."));

        // then
        assertThat(saveInstructor.getLoginId()).isEqualTo(instructor.getLoginId());
    }
}