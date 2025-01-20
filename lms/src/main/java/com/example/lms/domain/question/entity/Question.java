package com.example.lms.domain.question.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.answer.entity.Answer;
import com.example.lms.domain.quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 200)
    private String correct;

    @Column(nullable = false)
    private Integer point;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public static Question createQuestion(Quiz quiz, String content, String correct, Integer point) {
        Question question = new Question();
        question.quiz = quiz;
        question.content = content;
        question.correct = correct;
        question.point = point;
        quiz.getQuestions().add(question);
        return question;

    }
}