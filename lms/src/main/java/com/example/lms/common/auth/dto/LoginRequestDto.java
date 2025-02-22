package com.example.lms.common.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

@Getter
@NoArgsConstructor
@Schema(name = "LoginRequestDto : 로그인 요청 Dto")
public class LoginRequestDto {
    @Schema(description = "아이디는 입력되어야 합니다. 아이디는 영어 소문자와 숫자만 사용하여 4~20자리입니다", example = "helloworld")
    private String loginId;

    @Schema(description = "비밀번호는 입력되어야 합니다. 비밀번호는 8~16자리 수 입니다. 영문 대소문자, 숫자, 특수문자를 포함합니다.", example = "helloworld1234@")
    private String password;

    @Builder
    public LoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
