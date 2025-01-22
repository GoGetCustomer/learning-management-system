package com.example.lms.domain.lecture.controller;

import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.entity.Lecture;
import com.example.lms.domain.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/course/{courseId}/lecture")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "과정 내 강의 생성", description = "새로운 강의를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "강의 업로드 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lecture.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LectureCreateResponseDto> createLecture(
            @PathVariable Long courseId,
            @RequestPart("data") LectureCreateRequestDto request,
            @RequestPart("file") MultipartFile file) throws IOException {
            // 강의 생성
            LectureCreateResponseDto response = lectureService.createLecture(request, courseId, file);
            URI location = URI.create(String.format("/api/course/%d/lecture/%d", courseId, response.getId()));
            return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{lectureId}")
    @Operation(
                summary = "강의 삭제",
            description = "업로드된 강의를 삭제합니다."
    )
    public ResponseEntity<String> deleteLecture(
            @Parameter(description = "삭제하려는 강의 ID", required = true)
            @PathVariable Long lectureId,
            @Parameter(description = "강의가 존재하는 과정 ID", required = true)
            @PathVariable Long courseId) {
            lectureService.deleteLecture(lectureId, courseId);
                return ResponseEntity.ok("강의가 삭제되었습니다.");
    }

    @Operation(summary = "강의 조회", description = "특정 과정의 강의를 조회합니다.")
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureCreateResponseDto> getLecture(
            @Parameter(description = "조회하려는 과정 ID", required = true)
            @PathVariable Long courseId,
            @Parameter(description = "조회하려는 강의 ID", required = true)
            @PathVariable Long lectureId) {
        LectureCreateResponseDto responseDto = lectureService.getLectureById(courseId, lectureId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "강의 목록 조회", description = "특정 과정의 모든 강의를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LectureCreateResponseDto>> getAllLectures(
            @Parameter(description = "조회하려는 과정 ID", required = true)
            @PathVariable Long courseId) {
        List<LectureCreateResponseDto> responseList = lectureService.getAllLectures(courseId);
        return ResponseEntity.ok(responseList);
    }
}
