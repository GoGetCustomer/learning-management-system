package com.example.lms.domain.lecture.controller;

import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.entity.Lecture;
import com.example.lms.domain.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/course/{courseId}/lecture")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping(consumes = {"application/octet-stream", "multipart/form-data"})
    @Operation(summary = "과정 내 강의 생성", description = "새로운 강의를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "강의 업로드 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lecture.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<LectureCreateResponseDto> createLecture(
            @PathVariable Long courseId,
            @RequestPart("data") LectureCreateRequestDto request,
            @RequestPart("file") MultipartFile file) {

        try {
            // 강의 생성
            LectureCreateResponseDto response = lectureService.createLecture(request, courseId, file);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
