package com.example.lms.domain.quiz.entity;

import com.example.lms.common.base.BaseTimeEntity;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.question.entity.Question;
import com.example.lms.domain.student.entity.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "quiz_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 50)
    private String quizTitle;

    @Column(nullable = false)
    private LocalDateTime quizDueDate;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public static Quiz createQuiz(Course course, String quizTitle, LocalDateTime quizDueDate) {
        Quiz quiz = new Quiz();
        quiz.course = course;
        quiz.quizTitle = quizTitle;
        quiz.quizDueDate = quizDueDate;
        return quiz;
    }

    public void updateQuizInfo(String quizTitle, LocalDateTime quizDueDate) {
        this.quizTitle = quizTitle;
        this.quizDueDate = quizDueDate;
    }
}