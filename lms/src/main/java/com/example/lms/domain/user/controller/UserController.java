package com.example.lms.domain.user.controller;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.utils.CookieUtil;
import com.example.lms.common.validation.ValidationSequence;
import com.example.lms.domain.user.dto.UserUpdatePasswordRequestDto;
import com.example.lms.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.*;
import static com.example.lms.common.utils.CookieUtil.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(null);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {
        userService.delete(request);
        return ResponseEntity.status(CREATED)
                .header(COOKIE_PREFIX, createCookie(REFRESH_TOKEN_COOKIE_NAME, null, COOKIE_EXPIRATION_DELETE).toString())
                .body(null);
    }

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        Claims claimsByAccessToken = tokenProvider.getClaimsByAccessToken(accessToken);

        String role = claimsByAccessToken.get(AUTHORITIES_KEY).toString();
        String subject = claimsByAccessToken.getSubject();
        String requestRefreshToken = CookieUtil.findCookieByName(request, REFRESH_TOKEN_COOKIE_NAME).getValue();

        boolean isValid = tokenProvider.validateRefreshTokenWithAccessTokenInfo(role, subject, requestRefreshToken);
        if (!isValid) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .header(COOKIE_PREFIX, createCookie(REFRESH_TOKEN_COOKIE_NAME, null, COOKIE_EXPIRATION_DELETE).toString())
                    .body(null);
        }

        String newAccessToken = tokenProvider.createAccessToken(subject, role, new Date());
        String newRefreshToken = tokenProvider.createRefreshToken(subject, role, new Date());
        return ResponseEntity.status(OK)
                .header(AUTHORIZATION_HEADER, newAccessToken)
                .header(COOKIE_PREFIX, createCookie(REFRESH_TOKEN_COOKIE_NAME, newRefreshToken, tokenProvider.getRefreshTokenExpirationSeconds()).toString())
                .body(null);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Validated(ValidationSequence.class) @RequestBody UserUpdatePasswordRequestDto userUpdatePasswordRequestDto) {
        userService.updatePassword(userUpdatePasswordRequestDto);
        return ResponseEntity.status(CREATED)
                .header(COOKIE_PREFIX, createCookie(REFRESH_TOKEN_COOKIE_NAME, null, COOKIE_EXPIRATION_DELETE).toString())
                .body(null);
    }
}
