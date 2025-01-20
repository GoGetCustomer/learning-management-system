package com.example.lms.common.fixture;

import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.dto.InstructorInfo;
import com.example.lms.domain.instructor.entity.Instructor;
import lombok.Getter;

@Getter
public enum InstructorFixture {
    INSTRUCTOR_FIXTURE_1(
            "testLoginId",
            "password1234@",
            "test@gmail.com",
            "호주머니",
            "호주 개발자가 돈 많이 번다는대? 호주 8년차 개발자의 비즈니스 영어 노하우."),
    INSTRUCTOR_FIXTURE_2(
            "testLoginId2",
            "password1234@",
            "test2@gmail.com",
            "이재용",
            "걱정 끝. 미국주식 천재. 저는 매일 배당금으로 공짜 커피를 마셔요. (서울대학교 경제학과 출신의 명품 강의)"),
    INSTRUCTOR_FIXTURE_3(
            "testLoginId3",
            "password1234@",
            "test3@gmail.com",
            "바둑이",
            "저 는. 65 세 바둑기사 입 니다. 바둑은 기세입니다 .");

    private final String loginId;
    private final String password;
    private final String email;
    private final String name;
    private final String description;

    InstructorFixture(String loginId, String password, String email, String name, String description) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.description = description;
    }

    public Instructor createInstructor() {
        return Instructor.of(loginId, password, email, name, description);
    }

    public InstructorCreateRequestDto signupInstructor() {
        return InstructorCreateRequestDto.builder()
                .loginId(loginId)
                .password(password)
                .passwordCheck(password)
                .email(email)
                .name(name)
                .description(description)
                .build();
    }

    public InstructorInfo toInstructorInfo(Long instructorId) {
        return InstructorInfo.builder()
                .instructorId(instructorId)
                .name(name)
                .email(email)
                .description(description)
                .build();
    }
}
