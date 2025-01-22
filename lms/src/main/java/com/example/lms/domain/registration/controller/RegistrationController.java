package com.example.lms.domain.registration.controller;

import com.example.lms.domain.registration.serveice.RegistrationService;
import lombok.RequiredArgsConstructor;
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

    @PutMapping("/{registrationId}/courses/{courseId}")
    public ResponseEntity<Long> cancel(@PathVariable("registrationId") Long registrationId, @PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.cancelRegistration(registrationId, courseId));
    }
}
