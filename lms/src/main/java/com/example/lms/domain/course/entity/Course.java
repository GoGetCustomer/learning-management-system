package com.example.lms.domain.course.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.content.entity.Content;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.lecture.entity.Lecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="course")
@Getter
@NoArgsConstructor
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();


    /**
     * 등록 정보 (OneToMany 관계)
     *
     * @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
     * private List<Registration> registrations = new ArrayList<>();
     */

    @Builder
    public Course(Long courseId, String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, int courseCapacity, Instructor instructor) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseCapacity = courseCapacity;
        this.instructor = instructor;
    }

    public static Course of(String courseTitle, String courseDescription, LocalDate startDate, LocalDate endDate, int courseCapacity, Instructor instructor) {
        return Course.builder()
                .courseTitle(courseTitle)
                .courseDescription(courseDescription)
                .startDate(startDate)
                .endDate(endDate)
                .courseCapacity(courseCapacity)
                .instructor(instructor)
                .build();
    }
}
