package com.example.lms.domain.registration.serveice;

import com.example.lms.common.fixture.CourseFixture;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.enums.RegistrationStatus;
import com.example.lms.domain.registration.repository.RegistrationRepository;
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

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    private TeachingRepository teachingRepository;

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

    @Test
    @DisplayName("학생이 수강 신청을 철회한다.")
    void cancelRegistrationByStudentTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(student.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))));

        Registration registration = registrationRepository.save(Registration.of(student, course));

        //when
        registrationService.cancelRegistration(registration.getId(), course.getId());

        //then
        Registration canceledRegistration = registrationRepository.findById(registration.getId())
                .orElseThrow(() -> new IllegalArgumentException("수강 이력을 찾을 수 없습니다."));
        assertThat(canceledRegistration.getRegistrationStatus()).isEqualTo(RegistrationStatus.CANCELED);
    }

    @Test
    @DisplayName("강사가 수강 신청을 철회한다.")
    void cancelRegistrationByInstructorTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Registration registration = registrationRepository.save(Registration.of(student, course));

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(instructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        //when
        registrationService.cancelRegistration(registration.getId(), course.getId());

        //then
        Registration canceledRegistration = registrationRepository.findById(registration.getId())
                .orElseThrow(() -> new IllegalArgumentException("수강 이력을 찾을 수 없습니다."));
        assertThat(canceledRegistration.getRegistrationStatus()).isEqualTo(RegistrationStatus.CANCELED);
    }

    @Test
    @DisplayName("수강 신청인만 수강 신청 철회를 한다.")
    void cancelRegistrationUnauthorizedStudentTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Registration registration = registrationRepository.save(Registration.of(student, course));

        Student anotherStudent = studentRepository.save(StudentFixture.STUDENT_FIXTURE_2.createStudent());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(anotherStudent.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))));

        //when & then
        assertThatThrownBy(() -> registrationService.cancelRegistration(registration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과정 진행 강사 혹은 수강 신청인만 철회할 수 있습니다.");
    }

    @Test
    @DisplayName("강좌 담당 강사만 학생 수강 신청을 철회한다.")
    void cancelRegistrationUnauthorizedInstructorTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Registration registration = registrationRepository.save(Registration.of(student, course));

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        Instructor anotherInstructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_2.createInstructor());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(anotherInstructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        //when & then
        assertThatThrownBy(() -> registrationService.cancelRegistration(registration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과정 진행 강사 혹은 수강 신청인만 철회할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 철회된 수강 이력은 철회할 수 없다.")
    void cancelRegistrationAlreadyCanceledTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        student.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.STUDENT.getAuthority()))));

        Registration registration = Registration.of(student, course);
        registration.cancel();

        Registration cancelRegistration = registrationRepository.save(registration);

        //when & then
        assertThatThrownBy(() -> registrationService.cancelRegistration(cancelRegistration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 철회된 수강 이력입니다.");
    }

    @Test
    @DisplayName("강사는 수강 승인을 한다.")
    void approveRegistrationUnauthorizedInstructorTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Registration registration = registrationRepository.save(Registration.of(student, course));

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(instructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        //when
        registrationService.approveRegistration(registration.getId(), course.getId());
        Registration canceledRegistration = registrationRepository.findById(registration.getId())
                .orElseThrow(() -> new IllegalArgumentException("수강 이력을 찾을 수 없습니다."));
        assertThat(canceledRegistration.getRegistrationStatus()).isEqualTo(RegistrationStatus.APPROVED);
    }

    @Test
    @DisplayName("과정 진행 강사만 수강 승인을 한다.")
    void approveRegistrationTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());
        Registration registration = registrationRepository.save(Registration.of(student, course));

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        Instructor anotherInstructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_2.createInstructor());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(anotherInstructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        //when
        assertThatThrownBy(() -> registrationService.approveRegistration(registration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과정 진행 강사만 수강 신청을 승인할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 승인된 수강은 승인할 수 없다.")
    void approveRegistrationAlreadyApprovedTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        instructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        Registration registration = Registration.of(student, course);
        registration.approve();

        Registration approveRegistration = registrationRepository.save(registration);

        //when & then
        assertThatThrownBy(() -> registrationService.approveRegistration(approveRegistration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수강 승인 대기 상태가 아닙니다.");
    }

    @Test
    @DisplayName("이미 철회된 수강은 승인할 수 없다.")
    void approveRegistrationAlreadyCanceledTest() {
        //given
        Student student = studentRepository.save(StudentFixture.STUDENT_FIXTURE_1.createStudent());
        Course course = courseRepository.save(CourseFixture.COURSE_FIXTURE_1.createCourse());

        Instructor instructor = instructorRepository.save(InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor());
        teachingRepository.save(Teaching.of(instructor, course));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        instructor.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(Role.INSTRUCTOR.getAuthority()))));

        Registration registration = Registration.of(student, course);
        registration.cancel();

        Registration cancelRegistration = registrationRepository.save(registration);

        //when & then
        assertThatThrownBy(() -> registrationService.approveRegistration(cancelRegistration.getId(), course.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수강 승인 대기 상태가 아닙니다.");
    }
}