package com.example.lms.domain.quiz.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    // TODO : Course 엔티티와 연관 관계로 변경 예정
    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 50)
    private String quizTitle;

    @Column(nullable = false)
    private LocalDateTime quizDueDate;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public static Quiz createQuiz(Long courseId, String quizTitle, LocalDateTime quizDueDate) {
        Quiz quiz = new Quiz();
        quiz.courseId = courseId;
        quiz.quizTitle = quizTitle;
        quiz.quizDueDate = quizDueDate;
        return quiz;

    }

}