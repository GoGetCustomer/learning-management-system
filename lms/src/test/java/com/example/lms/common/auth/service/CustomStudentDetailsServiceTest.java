package com.example.lms.common.auth.service;

import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.lms.domain.user.enums.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomStudentDetailsServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CustomStudentDetailsService customStudentDetailsService;


    @Test
    @DisplayName("학생 아이디로 UserDetails 객체를 반환한다.")
    void loadUserByUsernameWithStudentLoginId() {
        // given
        Student student = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        String loginId = student.getLoginId();
        when(studentRepository.findByLoginId(loginId)).thenReturn(Optional.of(student));

        // when
        UserDetails userDetails = customStudentDetailsService.loadUserByUsername(loginId);

        // then
        assertAll(
                () -> assertThat(userDetails).isNotNull(),
                () -> assertThat(userDetails.getUsername()).isEqualTo(student.getLoginId()),
                () -> assertThat(userDetails.getAuthorities())
                        .hasSize(1)
                        .extracting("authority")
                        .containsExactly(STUDENT.getAuthority())
        );
        verify(studentRepository, times(1)).findByLoginId(loginId);
    }

    @DisplayName("아이디를 찾을 수 없는 학생은 오류를 반환한다.")
    @Test
    void loadUserByUsernameUserNotFound() {
        // given
        String loginId = "none";
        when(studentRepository.findByLoginId(loginId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> customStudentDetailsService.loadUserByUsername(loginId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Student not found : " + loginId);
        verify(studentRepository, times(1)).findByLoginId(loginId);
    }

}