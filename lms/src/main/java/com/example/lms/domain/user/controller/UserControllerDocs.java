package com.example.lms.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Users")
public interface UserControllerDocs {

    @Operation(summary = "로그아웃 요청", description = "**성공 응답 데이터:** 브라우저 쿠키 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "로그아웃 실패"),
    })
    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpServletRequest request);

    @Operation(summary = "회원탈퇴 요청", description = "**성공 응답 데이터:**  브라우저 쿠키 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 탈퇴 완료"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다. 재 로그인 후 탈퇴를 시도하세요."),
    })
    ResponseEntity<?> delete(HttpServletRequest request);

    @Operation(summary = "재발급 요청", description = "**성공 데이터:** 헤더의 `토큰` 및 쿠키," +
            "무결성 침해 토큰으로 간주 시 `Refresh Token 초기화 진행 후 재로그인`을 유도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "재발급 실패, 무결성이 침해되었습니다. 재 로그인이 필요합니다."),
    })
    ResponseEntity<?> reissue(HttpServletRequest request);
}
