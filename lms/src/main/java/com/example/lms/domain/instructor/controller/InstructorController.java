package com.example.lms.domain.instructor.controller;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.common.validation.ValidationSequence;
import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import com.example.lms.domain.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController implements InstructorControllerDocs{

    private final InstructorService instructorService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(OK).body(null);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> join(@Validated(ValidationSequence.class) @RequestBody InstructorCreateRequestDto instructorCreateRequestDto) {
        return ResponseEntity.status(CREATED).body(instructorService.join(instructorCreateRequestDto));
    }
}
