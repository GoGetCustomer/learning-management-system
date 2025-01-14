package com.example.lms.domain.answer.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    // TODO : Student 엔티티와 연관 관계로 변경 에정
    @Column(nullable = false)
    private Long studentId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 200)
    private String answer;
}
