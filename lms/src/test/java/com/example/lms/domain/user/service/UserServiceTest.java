package com.example.lms.domain.user.service;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;

import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.dto.UserUpdatePasswordRequestDto;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.example.lms.common.auth.jwt.TokenConstants.AUTHORIZATION_HEADER;
import static com.example.lms.common.auth.jwt.TokenConstants.BEARER_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Test
    @DisplayName("학생 비밀번호를 변경한다.")
    void updateStudentPassword() {
        //given
        String password = "testPassword@";
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newPassword1234@";

        Student student = studentRepository.save(Student.of("testLoginId", bCryptPasswordEncoder.encode(password), "test@gmail.com", "test"));
        Long id = student.getId();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, null,
                List.of(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserUpdatePasswordRequestDto dto = new UserUpdatePasswordRequestDto(
                password,
                newPassword,
                newPasswordCheck
        );

        //when
        userService.updatePassword(dto);

        //then
        Student updatedStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("학생을 찾을 수 없습니다."));
        assertThat(bCryptPasswordEncoder.matches(newPassword, updatedStudent.getPassword())).isTrue();
    }

    @Test
    @DisplayName("강사 비밀번호를 변경한다.")
    void updateInstructorPassword() {
        //given
        String password = "testPassword@";
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newPassword1234@";

        Instructor instructor = instructorRepository.save(Instructor.of("testLoginId", bCryptPasswordEncoder.encode(password), "test@gmail.com", "test", "testDescription"));
        Long id = instructor.getId();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, null,
                List.of(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserUpdatePasswordRequestDto dto = new UserUpdatePasswordRequestDto(
                password,
                newPassword,
                newPasswordCheck
        );

        //when
        userService.updatePassword(dto);

        //then
        Instructor updatedInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("강사를 찾을 수 없습니다."));
        assertThat(bCryptPasswordEncoder.matches(newPassword, updatedInstructor.getPassword())).isTrue();
    }

    @Test
    @DisplayName("비밀번호 확인이 일치하지 않으면 예외를 발생시킨다.")
    void failWhenNewPasswordCheckDoesNotMatch() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());

        Long id = student.getId();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, null,
                List.of(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String oldPassword = student.getPassword();
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newFailPassword1234@";

        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto = new UserUpdatePasswordRequestDto(
                oldPassword,
                newPassword,
                newPasswordCheck
        );

        //when & then
        assertThatThrownBy(() -> userService.updatePassword(userUpdatePasswordRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("새 비밀번호 확인이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("기존 비밀번호가 일치하지 않으면 예외를 발생시킨다.")
    void failWhenOldPasswordDoesNotMatch() {
        //given
        Student student = studentRepository.save(Student.of("testLoginId", bCryptPasswordEncoder.encode("password"), "test@gmail.com", "testName"));
        Long id = student.getId();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, null,
                List.of(new SimpleGrantedAuthority(Role.STUDENT.getAuthority())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String wrongPassword = "wrongPassword@";
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newPassword1234@";

        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto = new UserUpdatePasswordRequestDto(
                wrongPassword,
                newPassword,
                newPasswordCheck
        );

        //when & then
        assertThatThrownBy(() -> userService.updatePassword(userUpdatePasswordRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 비밀번호가 일치하지 않습니다.");
    }
}