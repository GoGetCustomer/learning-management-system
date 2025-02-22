package com.example.lms.domain.user.controller;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.auth.service.CustomInstructorDetailsService;
import com.example.lms.common.auth.service.CustomStudentDetailsService;
import com.example.lms.common.config.SecurityConfig;
import com.example.lms.common.config.WithMockCustom;
import com.example.lms.domain.instructor.repository.InstructorRepository;
import com.example.lms.domain.student.repository.StudentRepository;
import com.example.lms.domain.user.dto.UserUpdatePasswordRequestDto;
import com.example.lms.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.*;
import static com.example.lms.common.auth.jwt.TokenConstants.AUTHORITIES_KEY;
import static com.example.lms.common.auth.jwt.TokenConstants.AUTHORIZATION_HEADER;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    private final String ROLE_STUDENT = "ROLE_STUDENT";
    private final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    public final String USER_API_BASE_PATH = "/api/users";
    private final String TEST_SUBJECT = "1";
    private final String TEST_ACCESS_TOKEN = "testAccessToken";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    CustomStudentDetailsService customStudentDetailsService;

    @MockitoBean
    CustomInstructorDetailsService customInstructorDetailsService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    StudentRepository studentRepository;

    @MockitoBean
    InstructorRepository instructorRepository;

    @MockitoBean
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockCustom(role = ROLE_STUDENT)
    @DisplayName("학생 로그아웃 요청 테스트")
    void studentLogout() throws Exception {
        //given
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class)))
                .thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN))
                .thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_STUDENT);

        //when
        ResultActions actions = mockMvc.perform(
                post(USER_API_BASE_PATH + "/logout")
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
    }

    @Test
    @WithMockCustom(role = ROLE_INSTRUCTOR)
    @DisplayName("API 강사 로그아웃 요청 테스트")
    void instructorLogout() throws Exception {
        //given
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class)))
                .thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN))
                .thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_INSTRUCTOR);

        //when
        ResultActions actions = mockMvc.perform(
                post(USER_API_BASE_PATH + "/logout")
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
    }

    @Test
    @WithMockCustom(role = ROLE_STUDENT)
    @DisplayName("API 학생 회원 탈퇴 요청 테스트")
    void studentDelete() throws Exception {
        //given
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class)))
                .thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN))
                .thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_STUDENT);

        //when
        ResultActions actions = mockMvc.perform(
                delete(USER_API_BASE_PATH)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
        verify(userService).delete(any());
        verify(userService, Mockito.times(1)).delete(Mockito.any(HttpServletRequest.class));
    }

    @Test
    @WithMockCustom(role = ROLE_INSTRUCTOR)
    @DisplayName("API 강사 회원 탈퇴 요청 테스트")
    void instructorDelete() throws Exception {
        //given
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class)))
                .thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN))
                .thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_INSTRUCTOR);

        //when
        ResultActions actions = mockMvc.perform(
                delete(USER_API_BASE_PATH)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
        verify(userService).delete(any());
        verify(userService, Mockito.times(1)).delete(Mockito.any(HttpServletRequest.class));
    }

    @Test
    @WithMockCustom(role = ROLE_STUDENT)
    @DisplayName("API 토큰 재발급 요청 테스트")
    void reissueToken() throws Exception {
        // given
        String requestRefreshToken = "testRefreshToken";

        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class))).thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN)).thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_STUDENT);
        when(tokenProvider.validateRefreshTokenWithAccessTokenInfo(ROLE_STUDENT, TEST_SUBJECT, requestRefreshToken)).thenReturn(true);

        String newAccessToken = "newAccessToken";
        Mockito.when(tokenProvider.createAccessToken(eq(TEST_SUBJECT), eq(ROLE_STUDENT), any(Date.class))).thenReturn(newAccessToken);

        // when
        ResultActions actions = mockMvc.perform(
                get(USER_API_BASE_PATH + "/reissue")
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN)
                        .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, requestRefreshToken)));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(header().string(AUTHORIZATION_HEADER, newAccessToken))
                .andDo(print());
    }

    @Test
    @WithMockCustom(role = ROLE_STUDENT)
    @DisplayName("API 학생 비밀번호 변경 요청 테스트")
    void updateStudentPassword() throws Exception {
        //given
        String password = "password1234@";
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newPassword1234@";

        UserUpdatePasswordRequestDto requestDto = new UserUpdatePasswordRequestDto(
                password,
                newPassword,
                newPasswordCheck
        );

        String requestBody = objectMapper.writeValueAsString(requestDto);
        doNothing().when(userService).updatePassword(any(UserUpdatePasswordRequestDto.class));

        //when
        ResultActions actions = mockMvc.perform(
                put(USER_API_BASE_PATH + "/password")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
        verify(userService, times(1)).updatePassword(any(UserUpdatePasswordRequestDto.class));
    }

    @Test
    @WithMockCustom(role = ROLE_INSTRUCTOR)
    @DisplayName("API 강사 비밀번호 변경 요청 테스트")
    void updateInstructorPassword() throws Exception {
        //given
        String password = "password1234@";
        String newPassword = "newPassword1234@";
        String newPasswordCheck = "newPassword1234@";

        UserUpdatePasswordRequestDto requestDto = new UserUpdatePasswordRequestDto(
                password,
                newPassword,
                newPasswordCheck
        );

        String requestBody = objectMapper.writeValueAsString(requestDto);
        doNothing().when(userService).updatePassword(any(UserUpdatePasswordRequestDto.class));

        //when
        ResultActions actions = mockMvc.perform(
                put(USER_API_BASE_PATH + "/password")
                        .contentType("application/json")
                        .content(requestBody)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TEST_ACCESS_TOKEN));
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(print());
        verify(userService, times(1)).updatePassword(any(UserUpdatePasswordRequestDto.class));
    }
}
