package com.example.lms.domain.student.conrtoller;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.common.validation.ValidationSequence;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController implements StudentControllerDocs{
    private final StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(OK).body(null);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> join(@Validated(ValidationSequence.class) @RequestBody StudentCreateRequestDto studentCreateRequestDto) {
        return ResponseEntity.status(CREATED).body(studentService.join(studentCreateRequestDto));
    }
}
