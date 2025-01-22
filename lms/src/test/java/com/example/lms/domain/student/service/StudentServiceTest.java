package com.example.lms.domain.student.service;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.instructor.service.InstructorService;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.repository.RegistrationRepository;
import com.example.lms.domain.student.dto.StudentBasicInfoResponseDto;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.dto.StudentPersonalInfoResponseDto;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.teaching.entity.Teaching;
import com.example.lms.domain.teaching.repository.TeachingRepository;
import com.example.lms.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private TeachingRepository teachingRepository;

    @Test
    @DisplayName("학생으로 회원가입을 한다.")
    void studentJoinTest() throws Exception {
        //given
        StudentCreateRequestDto studentCreateRequestDto = StudentFixture.STUDENT_FIXTURE_1.signUpStudent();

        //when
        Long id = studentService.join(studentCreateRequestDto);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("찾을 수 없는 학생"));

        //then
        assertAll(
                () -> assertThat(studentCreateRequestDto.getLoginId()).isEqualTo(student.getLoginId()),
                () -> assertThat(studentCreateRequestDto.getName()).isEqualTo(student.getName()),
                () -> assertThat(studentCreateRequestDto.getEmail()).isEqualTo(student.getEmail())
        );
    }

    @Test
    @DisplayName("아이디가 중복되면 에러를 반환한다.")
    void studentCheckLoginIdTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        String loginId = student.getLoginId();

        //when & then
        assertThatThrownBy(() -> studentService.checkLoginIdDuplicate(loginId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이메일이 중복되면 에러를 반환한다.")
    void studentCheckEmailTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        String email = student.getEmail();

        //when & then
        assertThatThrownBy(() -> studentService.checkEmailDuplicate(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("학생이 개인정보를 조회한다.")
    void getPersonalInfoTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(student.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))));

        //when
        StudentPersonalInfoResponseDto studentPersonalInfoResponseDto = studentService.findPersonalInfo();

        //then
        assertAll(
                () -> assertThat(studentPersonalInfoResponseDto.getId()).isEqualTo(student.getId()),
                () -> assertThat(studentPersonalInfoResponseDto.getLoginId()).isEqualTo(student.getLoginId()),
                () -> assertThat(studentPersonalInfoResponseDto.getName()).isEqualTo(student.getName()),
                () -> assertThat(studentPersonalInfoResponseDto.getEmail()).isEqualTo(student.getEmail())
        );
    }

    @Test
    @DisplayName("해당 과정의 담당 강사가 학생 정보를 조회한다.")
    void getBasicInfoForInstructor() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(instructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());

        Registration registration = Registration.of(student, course);
        registrationRepository.save(registration);

        Teaching teaching = Teaching.of(instructor, course);
        teachingRepository.save(teaching);

        //when
        StudentBasicInfoResponseDto studentBasicInfoResponseDto = studentService.findBasicInfoForInstructor(student.getId(), course.getId());

        //then
        assertAll(
                () -> assertThat(studentBasicInfoResponseDto.getId()).isEqualTo(student.getId()),
                () -> assertThat(studentBasicInfoResponseDto.getName()).isEqualTo(student.getName()),
                () -> assertThat(studentBasicInfoResponseDto.getEmail()).isEqualTo(student.getEmail())
        );
    }

    @Test
    @DisplayName("해당 과정의 담당 강사가 아니면 학생 정보를 조회 할 수 없다.")
    void getBasicInfoAnotherInstructorTest() {
        //given
        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());

        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());

        Registration registration = Registration.of(student, course);
        registrationRepository.save(registration);

        Teaching teaching = Teaching.of(instructor, course);
        teachingRepository.save(teaching);

        Instructor anotherInstructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_2.createInstructor());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(anotherInstructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        //when & then
        assertThatThrownBy(() -> studentService.findBasicInfoForInstructor(student.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과정 진행 강사만 수강 학생 정보를 조회할 수 있습니다.");
    }
}