package com.example.lms.common.fixture;

import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.entity.Student;
import lombok.Getter;

@Getter
public enum StudentFixture {
    STUDENT_FIXTURE_1("testLoginId", "password1234@", "test@gmail.com", "홍길동"),
    STUDENT_FIXTURE_2("testLoginId2", "password1234@", "test2@gmail.com", "존도"),
    STUDENT_FIXTURE_3("testLoginId3", "password1234@", "test3@gmail.com", "제인도");

    private final String loginId;
    private final String password;
    private final String email;
    private final String name;

    StudentFixture(String loginId, String password, String email, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public Student createStudent() {
        return Student.of(loginId, password, email, name);
    }

    public StudentCreateRequestDto signUpStudent() {
        return StudentCreateRequestDto.builder()
                .loginId(loginId)
                .password(password)
                .passwordCheck(password)
                .email(email)
                .name(name)
                .build();
    }
}
