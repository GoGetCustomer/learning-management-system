package com.example.lms.domain.registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Registration")
public interface RegistrationControllerDocs {

    @Operation(summary = "수강 신청 요청", description = "성공 응답 student_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수강 신청 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<Long> register(Long courseId);

    @Operation(summary = "수강 신청 취소 요청", description = "성공 응답 registration_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수강 신청 취소 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<Long> cancel(Long registrationId, Long courseId);

    @Operation(summary = "수강 승인 요청", description = "성공 응답 registration_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수강 승인 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<Long> approve(Long registrationId, Long courseId);
}
