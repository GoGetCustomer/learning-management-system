package com.example.lms.domain.user.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.user.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class User extends BaseTimeEntity {

    @Column(name = "login_id", length = 20, nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private Role role;

    protected User(String loginId, String password, String email, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
