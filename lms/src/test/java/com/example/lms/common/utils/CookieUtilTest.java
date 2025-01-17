package com.example.lms.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {

    private static final String COOKIE_NAME = "cookieName";
    private static final String COOKIE_VALUE = "cookieValue";
    private static final long COOKIE_EXPIRATION = 86400;

    @Test
    @DisplayName("쿠키를 정상적으로 발급한다")
    public void createCookeTest() {
        // when
        ResponseCookie cookie = CookieUtil.createCookie(COOKIE_NAME, COOKIE_VALUE, COOKIE_EXPIRATION);

        // then
        assertAll(
                () -> assertThat(cookie).isNotNull(),
                () -> assertThat(cookie.getName()).isEqualTo(COOKIE_NAME),
                () -> assertThat(cookie.getValue()).isEqualTo(COOKIE_VALUE),
                () -> assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(COOKIE_EXPIRATION),
                () -> assertThat(cookie.getPath()).isEqualTo("/"),
                () -> assertThat(cookie.getSameSite()).isEqualTo("Strict"),
                () -> assertThat(cookie.isHttpOnly()).isTrue()
        );
    }

    @Test
    @DisplayName("찾는 이름의 쿠키가 요청에서 조회된다")
    public void findCookeByNameTest() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] mockCookies = {new Cookie(COOKIE_NAME, COOKIE_VALUE)};

        // when
        when(request.getCookies()).thenReturn(mockCookies);
        Cookie cookie = CookieUtil.findCookieByName(request, COOKIE_NAME);

        // then
        assertAll(
                () -> assertThat(cookie).isNotNull(),
                () -> assertThat(cookie.getName()).isEqualTo(COOKIE_NAME),
                () -> assertThat(cookie.getValue()).isEqualTo(COOKIE_VALUE)
        );
    }

    @Test
    @DisplayName("찾는 이름의 쿠키가 요청에 없을 경우 null을 반환한다")
    public void findCookieByNameNullTest() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] mockCookies = {new Cookie("differentCookie", COOKIE_VALUE)};
        when(request.getCookies()).thenReturn(mockCookies);

        // when
        Cookie cookie = CookieUtil.findCookieByName(request, COOKIE_NAME);

        // then
        assertThat(cookie).isNull();
    }
}