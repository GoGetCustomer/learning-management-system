package com.example.lms.domain.instructor.service;


import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.domain.instructor.dto.InstructorBasicInfoResponseDto;
import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.dto.InstructorPersonalInfoResponseDto;
import com.example.lms.domain.instructor.dto.InstructorUpdateRequestDto;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    @DisplayName("강사 개인정보를 조회한다.")
    void instructorPersonalInfoTest() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(instructor.getId(), null,
                List.of(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        InstructorPersonalInfoResponseDto personalInfo = instructorService.findPersonalInfo();

        //then
        assertAll(
                () -> assertThat(personalInfo.getId()).isEqualTo(instructor.getId()),
                () -> assertThat(personalInfo.getLoginId()).isEqualTo(instructor.getLoginId()),
                () -> assertThat(personalInfo.getName()).isEqualTo(instructor.getName()),
                () -> assertThat(personalInfo.getEmail()).isEqualTo(instructor.getEmail()),
                () -> assertThat(personalInfo.getDescription()).isEqualTo(instructor.getDescription())
        );
    }

    @Test
    @DisplayName("강사 기본정보를 조회한다.")
    void instructorBasicInfoTest() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());

        //when
        InstructorBasicInfoResponseDto basicInfo = instructorService.findBasicInfo(instructor.getId());

        //then
        assertAll(
                () -> assertThat(basicInfo.getName()).isEqualTo(instructor.getName()),
                () -> assertThat(basicInfo.getDescription()).isEqualTo(instructor.getDescription()),
                () -> assertThat(basicInfo.getEmail()).isEqualTo(instructor.getEmail())
        );
    }

    @Test
    @DisplayName("강사 정보를 수정한다.")
    void instructorUpdateTest() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(instructor.getId(), null,
                List.of(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String updateName = "김일타";
        String updateEmail = "hello@gmail.com";
        String updateDescription = "지금은 바빠서 나중에 개강하겠습니다.";
        InstructorUpdateRequestDto instructorUpdateRequestDto = new InstructorUpdateRequestDto(updateName, updateEmail, updateDescription);

        //when
        Long id = instructorService.update(instructorUpdateRequestDto);

        //then
        Instructor updateInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("강사를 찾지 못했습니다."));
        assertAll(
                () -> assertThat(updateInstructor.getName()).isEqualTo(updateName),
                () -> assertThat(updateInstructor.getEmail()).isEqualTo(updateEmail),
                () -> assertThat(updateInstructor.getDescription()).isEqualTo(updateDescription)
        );
    }
}