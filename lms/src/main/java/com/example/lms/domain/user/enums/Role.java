package com.example.lms.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    STUDENT("ROLE_STUDENT", "학생"),
    INSTRUCTOR("ROLE_INSTRUCTOR", "강사");

    private final String authority;
    private final String description;
}
