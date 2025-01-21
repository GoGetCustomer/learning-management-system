package com.example.lms.domain.teaching.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Teaching")
public interface TeachingControllerDocs {

    @Operation(summary = "수업 정보를 요청", description = "수업 데이털르 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<?> getAllTeaching(int page);
}
