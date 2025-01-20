package com.example.lms.domain.answer.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.student.entity.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "answer")
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 200)
    private String answer;

    public static Answer createAnswer(String answer, Student student, Question question) {
        Answer answerEntity = new Answer();
        answerEntity.question = question;
        answerEntity.answer = answer;
        answerEntity.student = student;
        return answerEntity;
    }
}