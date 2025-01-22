package com.example.lms.domain.student.entity;

import com.example.lms.domain.answer.entity.Answer;
import com.example.lms.domain.assignment.entity.AssignmentGrade;
import com.example.lms.domain.quizGrade.entity.QuizGrade;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.user.enums.Role;
import com.example.lms.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "student")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @OneToMany(mappedBy = "student", cascade = ALL)
    private List<Registration> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = ALL)
    private List<QuizGrade> quizGrades = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = ALL)
    private List<AssignmentGrade> assigmentGrades = new ArrayList<>();

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

    public void update(String name, String email) {
        super.updateUser(name, email);
    }
}
