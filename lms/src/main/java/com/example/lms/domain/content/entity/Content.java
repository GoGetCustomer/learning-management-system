package com.example.lms.domain.content.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.course.entity.Course;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Content extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "file_type", length = 100, nullable = false)
    private String fileType;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "file_url", length = 300, nullable = false)
    private String fileUrl;
}
