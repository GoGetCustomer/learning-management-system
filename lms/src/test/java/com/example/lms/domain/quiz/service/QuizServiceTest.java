package com.example.lms.domain.quiz.service;

import com.example.lms.domain.quiz.dto.QuizRequest;
import com.example.lms.domain.quiz.dto.QuizResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Test
    @Transactional
    @DisplayName("퀴즈를 생성한다")
    void testCreateQuiz() {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setQuizTitle("퀴즈 예시");
        quizRequest.setCourseId(1L);
        quizRequest.setQuizDueDate(LocalDateTime.now().plusDays(7));

        QuizRequest.QuestionRequest question1 = new QuizRequest.QuestionRequest();
        question1.setContent("10 + 10은 무엇인가요?");
        question1.setCorrect("20");
        question1.setPoint(10);

        QuizRequest.QuestionRequest question2 = new QuizRequest.QuestionRequest();
        question2.setContent("5 * 3은 무엇인가요?");
        question2.setCorrect("15");
        question2.setPoint(5);

        quizRequest.setQuestions(Arrays.asList(question1, question2));

        QuizResponse quizResponse = quizService.createQuiz(quizRequest);

        assertEquals("퀴즈 예시", quizResponse.getQuizTitle());
        assertEquals(2, quizResponse.getQuestions().size());
        assertEquals("10 + 10은 무엇인가요?", quizResponse.getQuestions().get(0).getContent());
        assertEquals("20", quizResponse.getQuestions().get(0).getCorrect());
        assertEquals("5 * 3은 무엇인가요?", quizResponse.getQuestions().get(1).getContent());
        assertEquals("15", quizResponse.getQuestions().get(1).getCorrect());
    }
}
