package com.example.lms.domain.student.controller;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.common.validation.ValidationSequence;
import com.example.lms.domain.student.dto.StudentBasicInfoResponseDto;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.dto.StudentPersonalInfoResponseDto;
import com.example.lms.domain.student.dto.StudentUpdateRequestDto;
import com.example.lms.domain.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/check-login-id/{loginId}")
    public ResponseEntity<Boolean> checkLoginId(@PathVariable("loginId") String loginId) {
        studentService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.status(OK).body(true);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable("email") String email) {
        studentService.checkEmailDuplicate(email);
        return ResponseEntity.status(OK).body(true);
    }

    @GetMapping
    public ResponseEntity<StudentPersonalInfoResponseDto> getPersonalInfo() {
        return ResponseEntity.status(OK).body(studentService.findPersonalInfo());
    }

    @GetMapping("/managements")
    public ResponseEntity<StudentBasicInfoResponseDto> getBasicInfo(@RequestParam Long studentId, @RequestParam Long courseId) {
        return ResponseEntity.status(OK).body(studentService.findBasicInfoForInstructor(studentId, courseId));
    }

    @PutMapping
    public ResponseEntity<Long> updateStudent(@Validated(ValidationSequence.class) @RequestBody StudentUpdateRequestDto studentUpdateRequestDto) {
        return ResponseEntity.status(OK).body(studentService.update(studentUpdateRequestDto));
    }
}
