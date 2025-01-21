package com.example.lms.domain.instructor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InstructorInfo {
    private Long instructorId;
    private String name;
    private String email;
    private String loginId;
    private String description;
    @Builder
    public InstructorInfo(Long instructorId, String loginId, String password, String passwordCheck, String name, String email, String description) {
        this.instructorId = instructorId;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.description = description;
    }
}
