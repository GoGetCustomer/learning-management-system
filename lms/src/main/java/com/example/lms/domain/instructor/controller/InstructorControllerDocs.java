package com.example.lms.domain.instructor.controller;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.domain.instructor.dto.InstructorCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Instructors")
@Validated
public interface InstructorControllerDocs {

    @Operation(summary = "강사 로그인 요청", description = "강사로 로그인을 진행하면 헤더 accessToken, 쿠키 refreshToken이 발급 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "강사 로그인 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto);

    @Operation(summary = "강사 회원가입 요청", description = "**성공 응답 데이터:** 강사의 `instructor_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "강사 회원 가입 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "아이디/이메일 중복")
    })
    public ResponseEntity<Long> join (InstructorCreateRequestDto instructorCreateRequestDto);
}
