package com.example.lms.domain.registration.serveice;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.enums.RegistrationStatus;
import com.example.lms.domain.registration.repository.RegistrationRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class RegistrationServiceTest {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Test
    @DisplayName("학생 수강 신청을 한다.")
    void registerStudentTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(student.getId(), null));

        //when
        Long id = registrationService.registerStudent(course.getId());

        //then
        boolean isRegistered = registrationRepository.existsByCourseIdAndStudentId(course.getId(), student.getId());
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수강 이력을 찾을 수 없습니다."));
        assertAll(
                () -> assertThat(registration.getRegistrationStatus()).isEqualTo(RegistrationStatus.REGISTERED),
                () -> assertThat(isRegistered).isTrue()
        );
    }
}