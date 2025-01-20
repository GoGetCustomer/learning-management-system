package com.example.lms.domain.instructor.service;


import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class InstructorServiceTest {

    @Autowired
    InstructorService instructorService;

    @Autowired
    InstructorRepository instructorRepository;

    @Test
    @DisplayName("강사로 회원가입을 한다.")
    void instructorJoinTest() throws Exception {
        //given
        InstructorCreateRequestDto instructorCreateRequestDto = InstructorFixture.INSTRUCTOR_FIXTURE_1.signupInstructor();

        //when
        Long id = instructorService.join(instructorCreateRequestDto);
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new Exception("강사를 찾을 수 없습니다."));

        //then
        assertAll(
                () -> assertThat(instructorCreateRequestDto.getLoginId()).isEqualTo(instructor.getLoginId()),
                () -> assertThat(instructorCreateRequestDto.getName()).isEqualTo(instructor.getName()),
                () -> assertThat(instructorCreateRequestDto.getEmail()).isEqualTo(instructor.getEmail()),
                () -> assertThat(instructorCreateRequestDto.getDescription()).isEqualTo(instructor.getDescription())
        );
    }
}