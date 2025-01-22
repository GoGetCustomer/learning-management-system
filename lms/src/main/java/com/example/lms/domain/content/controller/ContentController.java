package com.example.lms.domain.content.controller;

import com.example.lms.domain.content.dto.response.ContentResponseDto;
import com.example.lms.domain.content.service.ContentService;
import com.example.lms.domain.lecture.entity.Lecture;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/course/{courseId}/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "과정 내 강의자료 생성", description = "새로운 강의자료를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "강의 업로드 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lecture.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<ContentResponseDto> createContent(
            @PathVariable Long courseId,
            @RequestPart("file") MultipartFile file) throws IOException {
            ContentResponseDto response = contentService.addContent(courseId, file);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{contentId}")
    @Operation(
            summary = "강의자료 삭제",
            description = "업로드된 강의자료를 삭제합니다."
    )
    public ResponseEntity<String> deleteContent(
            @Parameter(description = "삭제하려는 강의자료 ID", required = true)
            @PathVariable Long contentId,
            @Parameter(description = "강의가 존재하는 과정 ID", required = true)
            @PathVariable Long courseId) {
        contentService.deleteContent(courseId, contentId);
        return ResponseEntity.ok("강의자료가 삭제되었습니다.");
    }

    @Operation(summary = "강의자료 조회", description = "특정 과정의 강의자료를 조회합니다.")
    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponseDto> getContentByContentId(
            @Parameter(description = "조회하려는 과정 ID", required = true)
            @PathVariable Long courseId,
            @Parameter(description = "조회하려는 강의  자료 ID", required = true)
            @PathVariable Long contentId) {
        ContentResponseDto response = contentService.getContent(courseId, contentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "강의자료 목록 조회", description = "특정 과정의 모든 강의자료를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ContentResponseDto>> getAllContents(
            @Parameter(description = "조회하려는 과정 ID", required = true)
            @PathVariable Long courseId) {
        List<ContentResponseDto> responseList = contentService.getAllContents(courseId);
        return ResponseEntity.ok(responseList);
    }

}
