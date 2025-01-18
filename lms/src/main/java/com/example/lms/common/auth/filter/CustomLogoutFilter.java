package com.example.lms.common.auth.filter;

import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.*;
import static com.example.lms.common.utils.CookieUtil.COOKIE_PREFIX;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public CustomLogoutFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!request.getRequestURI().equals("/api/users/logout") || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = tokenProvider.resolveAccessToken(request);
        Claims claimsByAccessToken = tokenProvider.getClaimsByAccessToken(accessToken);

        String id = claimsByAccessToken.getSubject();
        String role = claimsByAccessToken.get(AUTHORITIES_KEY).toString();

        tokenProvider.invalidateRefreshToken(role, id);

        log.info("{}-{}: logout ({})", id, role, new Date());
        response.addHeader(COOKIE_PREFIX, CookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, null, CookieUtil.COOKIE_EXPIRATION_DELETE).toString());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(null));
    }
}
