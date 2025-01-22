package com.example.lms.domain.registration.serveice;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.enums.RegistrationStatus;
import com.example.lms.domain.registration.repository.RegistrationRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.teaching.repository.TeachingRepository;
import com.example.lms.domain.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeachingRepository teachingRepository;

    @Transactional
    public Long registerStudent(Long courseId) {
        Long studentId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (isStudentRegisteredForCourse(courseId, studentId)) {
            throw new IllegalStateException("이미 수강 신청을 완료한 학생입니다.");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("수강 신청 할 과정을 찾지 못했습니다."));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾지 못했습니다."));
        return registrationRepository.save(Registration.of(student, course)).getId();
    }

    @Transactional
    public Long cancelRegistration(Long registrationId, Long courseId) {
        if (isRegistrationInStatus(registrationId, RegistrationStatus.CANCELED)) {
            throw new IllegalArgumentException("이미 철회된 수강 이력입니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long targetId = Long.valueOf(authentication.getName());
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (!isAuthorizedForCourse(role, targetId, courseId)) {
            throw new IllegalArgumentException("과정 진행 강사 혹은 수강 신청인만 철회할 수 있습니다.");
        }

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강 신청 내역을 찾지 못했습니다."));
        registration.cancel();
        return registration.getId();
    }

    @Transactional
    public Long approveRegistration(Long registrationId, Long courseId) {
        if (!isRegistrationInStatus(registrationId, RegistrationStatus.REGISTERED)) {
            throw new IllegalArgumentException("수강 승인 대기 상태가 아닙니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long instructorId = Long.valueOf(authentication.getName());
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (!role.equals(Role.INSTRUCTOR.getAuthority()) || !isAuthorizedInstructorForCourse(instructorId, courseId)) {
            throw new IllegalArgumentException("과정 진행 강사만 수강 신청을 승인할 수 있습니다.");
        }
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강 신청 내역을 찾지 못했습니다."));
        registration.approve();
        return registration.getId();
    }

    private boolean isAuthorizedForCourse(String role, Long targetId, Long courseId) {
        if (role.equals(Role.STUDENT.getAuthority())) {
            return isAuthorizedStudentForCourse(targetId, courseId);
        }
        if (role.equals(Role.INSTRUCTOR.getAuthority())) {
            return isAuthorizedInstructorForCourse(targetId, courseId);
        }
        return false;
    }

    private boolean isAuthorizedStudentForCourse(Long studentId, Long courseId) {
        return registrationRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }

    private boolean isAuthorizedInstructorForCourse(Long instructorId, Long courseId) {
        return teachingRepository.existsByCourseIdIdAndInstructorId(courseId, instructorId);
    }

    private boolean isStudentRegisteredForCourse(Long courseId, Long studentId) {
        return registrationRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }

    private boolean isRegistrationInStatus(Long registrationId, RegistrationStatus status) {
        return registrationRepository.existsByRegistrationIdAndStatus(registrationId, status);
    }
}
