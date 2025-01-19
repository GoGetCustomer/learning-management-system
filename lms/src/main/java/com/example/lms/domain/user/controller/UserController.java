package com.example.lms.domain.user.controller;

import com.example.lms.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.lms.common.auth.jwt.TokenConstants.*;
import static com.example.lms.common.utils.CookieUtil.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;

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
}
