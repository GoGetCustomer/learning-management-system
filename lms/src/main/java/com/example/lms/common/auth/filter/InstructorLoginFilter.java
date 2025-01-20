package com.example.lms.common.auth.filter;

import com.example.lms.common.auth.dto.LoginRequestDto;
import com.example.lms.common.auth.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;

@Slf4j
public class InstructorLoginFilter extends CustomUsernamePasswordAuthenticationFilter {

    public InstructorLoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(authenticationManager, tokenProvider, objectMapper);
        this.setFilterProcessesUrl("/api/instructors/login");
    }

    @Override
    protected void validateLoginRequestDto(LoginRequestDto loginReqDto) {
        if (loginReqDto.getLoginId() == null || !loginReqDto.getLoginId().matches("^[a-z0-9]{4,20}$")) {
            log.error("401error -> 올바르지 않은 강사 로그인 아이디 요청");
            throw new IllegalArgumentException("올바르지 않은 강사 로그인 아이디 요청");
        }
        if (loginReqDto.getPassword() == null || !loginReqDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$")) {
            log.error("401error -> 올바르지 않은 강사 로그인 비밀번호 요청");
            throw new IllegalArgumentException("올바르지 않은 강사 로그인 비밀번호 요청");
        }
    }
}

