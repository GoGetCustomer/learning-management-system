package com.example.lms.domain.registration.controller;

import com.example.lms.domain.registration.serveice.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerDocs{

    private final RegistrationService registrationService;

    @PostMapping("/courses/{courseId}")
    public ResponseEntity<Long> register(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.registerStudent(courseId));
    }
}
