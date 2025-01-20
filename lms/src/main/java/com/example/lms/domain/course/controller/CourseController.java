package com.example.lms.domain.course.controller;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "강좌 생성", description = "새로운 강좌를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "강좌 생성 성공", content = @Content(schema = @Schema(implementation = CourseCreateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    })
    public ResponseEntity<CourseCreateResponseDto> createCourse(@RequestBody @Valid CourseCreateRequestDto requestDto) {
        CourseCreateResponseDto response = courseService.createCourse(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "강좌 수정", description = "기존 강좌 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강좌 수정 성공", content = @Content(schema = @Schema(implementation = CourseUpdateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "강좌를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<CourseUpdateResponseDto> updateCourse(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseUpdateRequestDto requestDto) {
        CourseUpdateResponseDto response = courseService.updateCourse(courseId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "강좌 삭제", description = "특정 강좌를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "강좌 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "강좌를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "강좌 조회", description = "특정 강좌 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강좌 조회 성공", content = @Content(schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "강좌를 찾을 수 없음", content = @Content)

    })
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long courseId) {
        CourseResponseDto response = courseService.getCourseById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "강사별 강좌 조회", description = "특정 강사가 담당하는 모든 강좌를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강좌 목록 조회 성공", content = @Content(schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "강사를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<CourseResponseDto>> getCourseByInstructorId(@PathVariable Long instructorId) {
        List<CourseResponseDto> responses = courseService.getCourseByInstructorId(instructorId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
