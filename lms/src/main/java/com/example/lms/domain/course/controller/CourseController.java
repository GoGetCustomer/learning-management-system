package com.example.lms.domain.course.controller;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.service.CourseService;
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
    public ResponseEntity<CourseCreateResponseDto> createCourse(@RequestBody @Valid CourseCreateRequestDto requestDto) {
        CourseCreateResponseDto response = courseService.createCourse(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseUpdateResponseDto> updateCourse(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseUpdateRequestDto requestDto) {
        CourseUpdateResponseDto response = courseService.updateCourse(courseId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long courseId) {
        CourseResponseDto response = courseService.getCourseById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseResponseDto>> getCourseByInstructorId(@PathVariable Long instructorId) {
        List<CourseResponseDto> responses = courseService.getCourseByInstructorId(instructorId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
