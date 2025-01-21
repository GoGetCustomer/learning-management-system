package com.example.lms.domain.lecture.controller;

import com.example.lms.domain.lecture.dto.request.LectureCreateRequestDto;
import com.example.lms.domain.lecture.dto.response.LectureCreateResponseDto;
import com.example.lms.domain.lecture.service.LectureService;
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
