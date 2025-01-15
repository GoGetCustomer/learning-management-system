package com.example.lms.domain.quiz;

import com.example.lms.domain.quiz.dto.QuizRequest;
import com.example.lms.domain.quiz.dto.QuizResponse;
import com.example.lms.domain.quiz.service.QuizService;
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
    void testCreateQuiz() {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setQuizTitle("Sample Quiz");
        quizRequest.setCourseId(1L);
        quizRequest.setQuizDueDate(LocalDateTime.now().plusDays(7));

        QuizRequest.QuestionRequest question1 = new QuizRequest.QuestionRequest();
        question1.setContent("What is 10 + 10?");
        question1.setCorrect("20");
        question1.setPoint(10);

        QuizRequest.QuestionRequest question2 = new QuizRequest.QuestionRequest();
        question2.setContent("What is 5 * 3?");
        question2.setCorrect("15");
        question2.setPoint(5);

        quizRequest.setQuestions(Arrays.asList(question1, question2));

        QuizResponse quizResponse = quizService.createQuiz(quizRequest);

        assertEquals("Sample Quiz", quizResponse.getQuizTitle());
        assertEquals(2, quizResponse.getQuestions().size());
        assertEquals("What is 10 + 10?", quizResponse.getQuestions().get(0).getContent());
        assertEquals("20", quizResponse.getQuestions().get(0).getCorrect());
        assertEquals("What is 5 * 3?", quizResponse.getQuestions().get(1).getContent());
        assertEquals("15", quizResponse.getQuestions().get(1).getCorrect());
    }
}