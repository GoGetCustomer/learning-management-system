package com.example.lms.domain.teaching.controller;

import com.example.lms.domain.teaching.service.TeachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachings")
public class TeachingController implements TeachingControllerDocs{

    private final TeachingService teachingService;

    @GetMapping
    public ResponseEntity<?> getAllTeaching(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(teachingService.findAllTeaching(page));
    }

    @DeleteMapping("/{teachingId}")
    public ResponseEntity<?> deleteTeaching(@PathVariable("teachingId") Long id) {
        teachingService.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
