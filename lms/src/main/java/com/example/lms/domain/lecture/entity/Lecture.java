package com.example.lms.domain.lecture.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.course.entity.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "lecture")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "lecture_title", length = 50, nullable = false)
    private String lectureTitle;

    @Column(name = "lecture_description", columnDefinition = "TEXT")
    private String lectureDescription;

    @Column(name = "lecture_url", length = 300, nullable = false)
    private String lectureUrl;

    @Column(name = "lecture_time", nullable = false)
    private LocalTime lectureTime;
}