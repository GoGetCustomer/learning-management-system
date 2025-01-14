package com.example.lms.domain.instructor.entity;

import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.lms.common.fixture.InstructorFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InstructorTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    @DisplayName("강사 엔티티 저장 테스트")
    void instructorEntitySaveTest() {
        //given
        Instructor instructor = INSTRUCTOR_FIXTURE_1.createInstructor();

        //when
        instructorRepository.save(instructor);

        //then
        Instructor saveInstructor = instructorRepository.findById(instructor.getId()).orElseThrow();

        assertAll(
                () -> assertThat(saveInstructor.getId()).isInstanceOf(Long.class),
                () -> assertThat(saveInstructor.getLoginId()).isEqualTo(instructor.getLoginId()),
                () -> assertThat(saveInstructor.getPassword()).isEqualTo(instructor.getPassword()),
                () -> assertThat(saveInstructor.getEmail()).isEqualTo(instructor.getEmail()),
                () -> assertThat(saveInstructor.getName()).isEqualTo(instructor.getName()),
                () -> assertThat(saveInstructor.getDescription()).isEqualTo(instructor.getDescription()),
                () -> assertThat(saveInstructor.getRole()).isEqualTo(Role.INSTRUCTOR)
        );
    }
}