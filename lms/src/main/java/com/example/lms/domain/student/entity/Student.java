package com.example.lms.domain.student.entity;

import com.example.lms.domain.user.enums.Role;
import com.example.lms.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Builder
    private Student(String loginId, String password, String email, String name) {
        super(loginId, password, email, name, Role.STUDENT);
    }

    public static Student of(String loginId, String password, String email, String name) {
        return Student.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .build();
    }
}
