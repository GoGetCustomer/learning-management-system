package com.example.lms.domain.course.controller;

import com.example.lms.common.config.WithMockCustom;
import com.example.lms.common.fixture.CourseFixture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.lms.domain.course.dto.request.CourseCreateRequestDto;
import com.example.lms.domain.course.dto.request.CourseUpdateRequestDto;
import com.example.lms.domain.course.dto.response.CourseCreateResponseDto;
import com.example.lms.domain.course.dto.response.CourseResponseDto;
import com.example.lms.domain.course.dto.response.CourseUpdateResponseDto;
import com.example.lms.domain.course.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseCreateRequestDto createRequestDto;
    private CourseUpdateRequestDto updateRequestDto;
    private CourseCreateResponseDto createResponseDto;
    private CourseUpdateResponseDto updateResponseDto;
    private CourseResponseDto courseResponseDto;
    private final String ROLE_STUDENT = "ROLE_STUDENT";
    private final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    @BeforeEach
    void setUp() {
        createRequestDto = CourseFixture.COURSE_FIXTURE_1.toCreateRequestDto(1L);
        updateRequestDto = CourseFixture.COURSE_FIXTURE_2.toUpdateRequestDto(1L);
        createResponseDto = CourseFixture.COURSE_FIXTURE_1.toCreateResponseDto(1L);
        updateResponseDto = CourseFixture.COURSE_FIXTURE_2.toUpdateResponseDto(1L);
        courseResponseDto = CourseFixture.COURSE_FIXTURE_1.toCourseResponseDto(1L);
    }

    @Test
    @DisplayName("강좌 생성 - 성공")
    @WithMockCustom(id = 1L, role = ROLE_INSTRUCTOR)
    void createCourse_success() throws Exception {
        when(courseService.createCourse(any())).thenReturn(createResponseDto);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createResponseDto.getId()))
                .andExpect(jsonPath("$.courseTitle").value(createResponseDto.getCourseTitle()))
                .andExpect(jsonPath("$.courseDescription").value(createResponseDto.getCourseDescription()));

        verify(courseService, times(1)).createCourse(any());
    }

    @Test
    @DisplayName("강좌 삭제 - 성공")
    @WithMockCustom(role = ROLE_INSTRUCTOR)
    void deleteCourse_success() throws Exception {
        mockMvc.perform(delete("/api/course/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    @DisplayName("강좌 수정 - 성공")
    @WithMockCustom(role = ROLE_INSTRUCTOR)
    void updateCourse_success() throws Exception {
        when(courseService.updateCourse(eq(1L), any())).thenReturn(updateResponseDto);

        mockMvc.perform(put("/api/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateResponseDto.getId()))
                .andExpect(jsonPath("$.courseTitle").value(updateResponseDto.getCourseTitle()))
                .andExpect(jsonPath("$.courseDescription").value(updateResponseDto.getCourseDescription()));

        verify(courseService, times(1)).updateCourse(eq(1L), any());
    }
    @Test
    @DisplayName("강좌 조회 - 성공")
    void getCourseById_success() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(courseResponseDto);

        mockMvc.perform(get("/api/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseResponseDto.getId()))
                .andExpect(jsonPath("$.courseTitle").value(courseResponseDto.getCourseTitle()))
                .andExpect(jsonPath("$.courseDescription").value(courseResponseDto.getCourseDescription()));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    @DisplayName("강사 ID로 강좌 목록 조회 - 성공")
    void getCourseByInstructorId_success() throws Exception {
        List<CourseResponseDto> responses = List.of(courseResponseDto);
        when(courseService.getCourseByInstructorId(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/course/instructor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(courseResponseDto.getId()))
                .andExpect(jsonPath("$[0].courseTitle").value(courseResponseDto.getCourseTitle()));

        verify(courseService, times(1)).getCourseByInstructorId(1L);
    }
}
