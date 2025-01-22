package com.example.lms.domain.student.controller;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.domain.student.dto.StudentCreateRequestDto;
import com.example.lms.domain.student.dto.StudentPersonalInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Students")
@Validated
public interface StudentControllerDocs {

    @Operation(summary = "학생 로그인 요청", description = "학생으로 로그인을 진행하면 헤더 accessToken, 쿠키 refreshToken이 발급 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "학생 로그인 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto);

    @Operation(summary = "학생 회원가입 요청", description = "**성공 응답 데이터:** 학생의 `student_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "학생 회원 가입 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "아이디/이메일 중복")
    })
    public ResponseEntity<Long> join (StudentCreateRequestDto studentCreateRequestDto);

    @Operation(summary = "학생 아이디 중복 확인 요청", description = "**성공 응답 데이터:** true ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "409", description = "아이디 중복"),
    })
    public ResponseEntity<Boolean> checkLoginId(String loginId);

    @Operation(summary = "학생 이메일 중복 확인 요청", description = "**성공 응답 데이터:** true ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
            @ApiResponse(responseCode = "409", description = "이메일 중복"),
    })
    public ResponseEntity<Boolean> checkEmail(String email);

    @Operation(summary = "회원 개인정보 조회 요청", description = " 회원의 개인 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 개인정보 조회 완료"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
    })
    public ResponseEntity<StudentPersonalInfoResponseDto> getPersonalInfo();
}
