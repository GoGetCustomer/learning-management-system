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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Test
    @Transactional
    @DisplayName("퀴즈를 생성하고 조회한다.")
    void testCreateAndGetQuiz() {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setQuizTitle("퀴즈 생성 및 조회 테스트");
        quizRequest.setCourseId(1L);
        quizRequest.setQuizDueDate(LocalDateTime.now().plusDays(7));

        QuizRequest.QuestionRequest question1 = new QuizRequest.QuestionRequest();
        question1.setContent("10 + 10은?");
        question1.setCorrect("20");
        question1.setPoint(10);

        QuizRequest.QuestionRequest question2 = new QuizRequest.QuestionRequest();
        question2.setContent("5 * 3은?");
        question2.setCorrect("15");
        question2.setPoint(5);

        quizRequest.setQuestions(Arrays.asList(question1, question2));

        QuizResponse createdQuiz = quizService.createQuiz(quizRequest);

        System.out.println("생성된 퀴즈 ID: " + createdQuiz.getQuizId());
        System.out.println("생성된 질문 수 : " + createdQuiz.getQuestions().size());
        createdQuiz.getQuestions().forEach(q ->
                System.out.println("질문 : " + q.getContent())
        );

        QuizResponse getQuiz = quizService.getQuiz(createdQuiz.getQuizId());

        assertNotNull(getQuiz, "퀴즈 조회 실패");
        assertEquals(createdQuiz.getQuizTitle(), getQuiz.getQuizTitle());
        assertEquals(createdQuiz.getQuestions().size(), getQuiz.getQuestions().size());
    }

    @Test
    @Transactional
    @DisplayName("특정 과정에 속한 퀴즈 목록을 조회한다.")
    void testGetQuizzesByCourseId() {
        QuizRequest quizRequest1 = new QuizRequest();
        quizRequest1.setQuizTitle("과정 1 퀴즈 1");
        quizRequest1.setCourseId(1L);
        quizRequest1.setQuizDueDate(LocalDateTime.now().plusDays(7));

        QuizRequest.QuestionRequest question1 = new QuizRequest.QuestionRequest();
        question1.setContent("1 + 1은?");
        question1.setCorrect("2");
        question1.setPoint(10);

        quizRequest1.setQuestions(List.of(question1));
        quizService.createQuiz(quizRequest1);

        QuizRequest quizRequest2 = new QuizRequest();
        quizRequest2.setQuizTitle("과정 1 퀴즈 2");
        quizRequest2.setCourseId(1L);
        quizRequest2.setQuizDueDate(LocalDateTime.now().plusDays(7));

        QuizRequest.QuestionRequest question2 = new QuizRequest.QuestionRequest();
        question2.setContent("2 + 2은?");
        question2.setCorrect("4");
        question2.setPoint(5);

        quizRequest2.setQuestions(List.of(question2));
        quizService.createQuiz(quizRequest2);

        List<QuizResponse> quizzes = quizService.getQuizzesByCourseId(1L);

        System.out.println("조회된 퀴즈 수 : " + quizzes.size());
        quizzes.forEach(q ->
                System.out.println("퀴즈 제목: " + q.getQuizTitle())
        );

        assertNotNull(quizzes);
        assertEquals(2, quizzes.size());
        assertEquals("과정 1 퀴즈 1", quizzes.get(0).getQuizTitle());
        assertEquals("과정 1 퀴즈 2", quizzes.get(1).getQuizTitle());
    }
}
