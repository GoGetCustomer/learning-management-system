package com.example.lms.domain.user.service;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;

import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.AUTHORIZATION_HEADER;
import static com.example.lms.common.auth.jwt.TokenConstants.BEARER_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Test
    @DisplayName("학생이 회원 탈퇴를 한다.")
    void deleteStudent() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        String accessToken = tokenProvider.createAccessToken(student.getId().toString(), Role.STUDENT.getAuthority(), new Date());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);

        //when
        userService.delete(request);

        //then
        Student deletedStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalStateException("학생을 찾을 수 없습니다."));

        assertThat(deletedStudent.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("강사가 회원 탈퇴를 한다.")
    void deleteInstructor() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        String accessToken = tokenProvider.createAccessToken(instructor.getId().toString(), Role.INSTRUCTOR.getAuthority(), new Date());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);

        //when
        userService.delete(request);

        //then
        Instructor deleteInstructor = instructorRepository.findById(instructor.getId())
                .orElseThrow(() -> new IllegalStateException("강사을 찾을 수 없습니다."));

        assertThat(deleteInstructor.getIsDeleted()).isTrue();
    }
}