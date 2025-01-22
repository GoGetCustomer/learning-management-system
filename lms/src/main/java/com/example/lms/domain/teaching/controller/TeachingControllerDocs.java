package com.example.lms.domain.teaching.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Teaching")
public interface TeachingControllerDocs {

    @Operation(summary = "수업 정보를 요청", description = "강사가 자신의 진행할 수업 목록을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<?> getAllTeaching(int page);

    @Operation(summary = "수업 삭제 요청", description = "수업을 진행 중인 강사만 수업을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수업 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없습니다.")
    })
    public ResponseEntity<?> deleteTeaching(Long id);
}
