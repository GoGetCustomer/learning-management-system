package com.example.lms.domain.registration.controller;

import com.example.lms.domain.registration.dto.RegistrationCourseResponseDto;
import com.example.lms.domain.registration.dto.RegistrationStudentResponseDto;
import com.example.lms.domain.registration.serveice.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerDocs{

    private final RegistrationService registrationService;

    @PostMapping("/courses/{courseId}")
    public ResponseEntity<Long> register(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.registerStudent(courseId));
    }

    @PutMapping("/{registrationId}/courses/{courseId}/cancel")
    public ResponseEntity<Long> cancel(@PathVariable("registrationId") Long registrationId, @PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.cancelRegistration(registrationId, courseId));
    }

    @PutMapping("/{registrationId}/courses/{courseId}/approve")
    public ResponseEntity<Long> approve(@PathVariable("registrationId") Long registrationId, @PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.approveRegistration(registrationId, courseId));
    }

    @GetMapping("/student/history")
    public ResponseEntity<Page<RegistrationStudentResponseDto>> getRegistrationStudentHistory(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.findStudentRegistrationHistory(page));
    }

    @GetMapping("/history/courses/{courseId}")
    public ResponseEntity<Page<RegistrationCourseResponseDto>> getRegistrationCourseHistory(@RequestParam(defaultValue = "1") int page,
                                                                                             @PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.findCourseRegistrationHistory(page, courseId));
    }
}
