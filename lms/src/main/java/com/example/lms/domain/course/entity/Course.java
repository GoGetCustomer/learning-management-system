package com.example.lms.domain.course.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.content.entity.Content;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.lecture.entity.Lecture;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.teaching.entity.Teaching;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="course")
@Getter
@NoArgsConstructor
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_title", length = 50, nullable = false)
    private String courseTitle;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

    @Column(name = "course_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "course_end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "course_capacity", nullable = false)
    private Integer courseCapacity;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teaching> teachings = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

    @Builder
    public Course(Long id, String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, int courseCapacity) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseCapacity = courseCapacity;
    }

    public static Course of(String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, int courseCapacity, List<Teaching> teachings) {
        Course course = Course.builder()
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .build();
        if (teachings != null) {
            teachings.forEach(course::addTeaching);
        }
        return course;
    }

    public void updateCourse(String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, Integer courseCapacity) {
        Optional.ofNullable(courseTitle).ifPresent(title -> this.courseTitle = title);
        Optional.ofNullable(courseDescription).ifPresent(description -> this.courseDescription = description);
        Optional.ofNullable(startDate).ifPresent(date -> this.startDate = date);
        Optional.ofNullable(endDate).ifPresent(date -> this.endDate = date);
        Optional.ofNullable(courseCapacity).ifPresent(capacity -> this.courseCapacity = capacity);
    }

    public boolean belongsToInstructor(Instructor instructor) {
        return this.teachings.stream()
                .anyMatch(teaching -> teaching.getInstructor().equals(instructor));
    }

    public void addTeaching(Teaching teaching) {
        if (teaching == null) {
            throw new IllegalArgumentException("Teaching cannot be null");
        }

        if (!this.teachings.contains(teaching)) {
            this.teachings.add(teaching);
            teaching.setCourse(this); // Teaching과의 양방향 동기화
        }
    }

}
