package com.example.lms.domain.registration.serveice;

import com.example.lms.domain.course.entity.Course;
import com.example.lms.domain.course.repository.CourseRepository;
import com.example.lms.domain.registration.entity.Registration;
import com.example.lms.domain.registration.repository.RegistrationRepository;
import com.example.lms.domain.student.entity.Student;
import com.example.lms.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Long registerStudent(Long courseId) {
        Long studentId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        if (isAlreadyRegistered(courseId, studentId)) {
            throw new IllegalStateException("이미 수강 신청을 완료한 학생입니다.");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("수강 신청 할 과정을 찾지 못했습니다."));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾지 못했습니다."));
        return registrationRepository.save(Registration.of(student, course)).getId();
    }

    private boolean isAlreadyRegistered(Long courseId, Long studentId) {
        return registrationRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }
}
