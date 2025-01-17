package com.example.lms.common.auth.service;

import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.reflection.ReflectionFieldSetter;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
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
class CustomInstructorDetailsServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private CustomInstructorDetailsService customInstructorDetailsService;


    @Test
    @DisplayName("강사 아이디로 UserDetails 객체를 반환한다.")
    void loadUserByUsernameWithInstructorLoginId() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        ReflectionFieldSetter.setId(instructor, 1L);
        String loginId = instructor.getLoginId();
        when(instructorRepository.findByLoginId(loginId)).thenReturn(Optional.of(instructor));

        // when
        UserDetails userDetails = customInstructorDetailsService.loadUserByUsername(loginId);

        // then
        assertAll(
                () -> assertThat(userDetails).isNotNull(),
                () -> assertThat(userDetails.getUsername()).isEqualTo(instructor.getId().toString()),
                () -> assertThat(userDetails.getAuthorities())
                        .hasSize(1)
                        .extracting("authority")
                        .containsExactly(INSTRUCTOR.getAuthority())
        );
        verify(instructorRepository, times(1)).findByLoginId(loginId);
    }

    @DisplayName("아이디를 찾을 수 없는 강사는 오류를 반환한다.")
    @Test
    void loadUserByUsernameUserNotFound() {
        // given
        String loginId = "none";
        when(instructorRepository.findByLoginId(loginId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> customInstructorDetailsService.loadUserByUsername(loginId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("강사 정보를 찾을 수 없습니다. : " + loginId);
        verify(instructorRepository, times(1)).findByLoginId(loginId);
    }

    @Test
    @DisplayName("강사 식별자로 UserDetails 객체를 반환한다.")
    void loadUserByUsernameWithInstructorId() {
        // given
        Instructor instructor = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        ReflectionFieldSetter.setId(instructor, 1L);
        Long instructorId = instructor.getId();
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        // when
        UserDetails userDetails = customInstructorDetailsService.loadUserByInstructorId(instructorId.toString());

        // then
        assertAll(
                () -> assertThat(userDetails).isNotNull(),
                () -> assertThat(userDetails.getUsername()).isEqualTo(instructor.getId().toString()),
                () -> assertThat(userDetails.getAuthorities())
                        .hasSize(1)
                        .extracting("authority")
                        .containsExactly(INSTRUCTOR.getAuthority())
        );
        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @DisplayName("식별자로 찾을 수 없는 강사는 오류를 반환한다.")
    @Test
    void loadUserByInstructorIdUserNotFound() {
        // given
        String instructorId = "1";
        when(instructorRepository.findById(Long.valueOf(instructorId))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> customInstructorDetailsService.loadUserByInstructorId(instructorId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("강사 정보를 찾을 수 없습니다. : " + instructorId);
        verify(instructorRepository, times(1)).findById(Long.valueOf(instructorId));
    }

}