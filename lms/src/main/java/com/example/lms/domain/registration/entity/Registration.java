package com.example.lms.domain.registration.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.registration.enums.RegistrationStatus;
import com.example.lms.domain.student.entity.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "registration")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", length = 20, nullable = false)
    private RegistrationStatus registrationStatus;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    private Registration(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public static Registration of(RegistrationStatus registrationStatus) {
        return Registration.builder()
                .build();
    }
}