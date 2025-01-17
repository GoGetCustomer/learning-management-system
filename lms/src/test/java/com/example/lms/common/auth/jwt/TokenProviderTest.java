package com.example.lms.common.auth.jwt;

import com.example.lms.common.auth.service.CustomInstructorDetailsService;
import com.example.lms.common.auth.service.CustomStudentDetailsService;
import com.example.lms.common.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static com.example.lms.common.auth.jwt.TokenConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    private TokenProvider tokenProvider;

    private static final String TEST_ACCESS_SECRET = "accessabcdefghijklmnopqrstuvwxyz";
    private static final String TEST_REFRESH_SECRET = "refreshabcdefghijklmnopqrstuvwxyz";
    private static final long TEST_EXPIRATION = 3600;
    private static final String TEST_SUBJECT = "1";
    private static final String TEST_ROLE_STUDENT = "ROLE_STUDENT";
    private static final String TEST_ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";

    @Mock
    private RedisService redisService;

    @Mock
    private CustomStudentDetailsService customStudentDetailsService;

    @Mock
    private CustomInstructorDetailsService customInstructorDetailsService;


    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(
                redisService,
                customStudentDetailsService,
                customInstructorDetailsService,
                TEST_ACCESS_SECRET,
                TEST_REFRESH_SECRET,
                TEST_EXPIRATION,
                TEST_EXPIRATION
        );
    }

    @Test
    @DisplayName("accessToken을 생성한다")
    void generateAccessToken() {
        //when
        String accessToken = tokenProvider.createAccessToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());
        
        Claims claims = parseTokenSubject(accessToken, TEST_ACCESS_SECRET);
        String subject = claims.getSubject();
        String role = claims.get(AUTHORITIES_KEY).toString();
        
        //then
        assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(subject).isEqualTo(TEST_SUBJECT),
                () -> assertThat(role).isEqualTo(TEST_ROLE_STUDENT)
        );
    }

    @Test
    @DisplayName("refreshToken을 생성한다")
    void generateRefreshToken() {
        //when
        String refreshToken = tokenProvider.createRefreshToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());
        String redisKey = TEST_ROLE_STUDENT + REDIS_PREFIX_REFRESH + TEST_SUBJECT;
        String subject = parseTokenSubject(refreshToken, TEST_REFRESH_SECRET).getSubject();

        //then
        assertAll(
                () -> assertThat(refreshToken).isNotNull(),
                () -> assertThat(subject).isEqualTo(TEST_SUBJECT)
        );
        verify(redisService, times(1)).saveRefreshToken(redisKey, refreshToken, TEST_EXPIRATION);
    }

    @Test
    @DisplayName("학생 권한 정보를 accessToken으로 조회한다")
    void getStudentAuthenticationByAccessToken() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());
        UserDetails userDetails = new User(TEST_SUBJECT, "", Collections.singletonList(new SimpleGrantedAuthority(TEST_ROLE_STUDENT)));
        when(customStudentDetailsService.loadUserByStudentId(TEST_SUBJECT)).thenReturn(userDetails);

        //when
        Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);

        //then
        assertAll(
                () -> assertThat(authentication).isNotNull(),
                () -> assertThat(authentication.getName()).isEqualTo(TEST_SUBJECT),
                () -> assertThat(authentication.getAuthorities())
                        .hasSize(1)
                        .extracting(authority -> authority.getAuthority())
                        .containsExactly(TEST_ROLE_STUDENT)
        );
    }

    @Test
    @DisplayName("강사 권한 정보를 accessToken으로 조회한다")
    void getInstructorAuthenticationByAccessToken() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_SUBJECT, TEST_ROLE_INSTRUCTOR, new Date());
        UserDetails userDetails = new User(TEST_SUBJECT, "", Collections.singletonList(new SimpleGrantedAuthority(TEST_ROLE_INSTRUCTOR)));
        when(customInstructorDetailsService.loadUserByInstructorId(TEST_SUBJECT)).thenReturn(userDetails);

        //when
        Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);

        //then
        assertAll(
                () -> assertThat(authentication).isNotNull(),
                () -> assertThat(authentication.getName()).isEqualTo(TEST_SUBJECT),
                () -> assertThat(authentication.getAuthorities())
                        .hasSize(1)
                        .extracting(authority -> authority.getAuthority())
                        .containsExactly(TEST_ROLE_INSTRUCTOR)
        );
    }

    @Test
    @DisplayName("accessToken으로 Claims를 조회한다")
    void getClaimsByAccessToken() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());

        //when
        Claims claims = tokenProvider.getClaimsByAccessToken(accessToken);
        String subject = claims.getSubject();
        String role = claims.get(AUTHORITIES_KEY).toString();


        //then
        assertAll(
                () -> assertThat(subject).isEqualTo(TEST_SUBJECT),
                () -> assertThat(role).isEqualTo(TEST_ROLE_STUDENT)
        );
    }

    @Test
    @DisplayName("refreshToken으로 Claims를 조회한다")
    void getClaimsByRefreshToken() {
        //given
        String refreshToken = tokenProvider.createRefreshToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());

        //when
        Claims claims = tokenProvider.getClaimsRefreshToken(refreshToken);
        String subject = claims.getSubject();

        //then
        assertThat(subject).isEqualTo(TEST_SUBJECT);
    }

    @Test
    @DisplayName("요청에서 accessToken을 추출한다")
    void resolveAccessToken() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION_HEADER))
                .thenReturn(BEARER_PREFIX + "testAccessToken");

        //when
        String accessToken = tokenProvider.resolveAccessToken(request);
        
        //then
        assertThat(accessToken).isEqualTo("testAccessToken");
    }

    @Test
    @DisplayName("요청의 accessToken에 Bearer이 포함되지않으면 null을 반환한다")
    void bearerIsNotIncludedInAccessToken() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn("noBearer testAccessToken");

        // when
        String result = tokenProvider.resolveAccessToken(request);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("accessToken의 유효성을 검증한다")
    void validateAccessToken() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());

        //when
        boolean isValid = tokenProvider.validateAccessToken(accessToken);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 accessToekn을 검증한다")
    void validateExpiredAccessToken() {
        // given
        String expiredAccessToken = tokenProvider.createAccessToken(
                TEST_SUBJECT,
                TEST_ROLE_STUDENT,
                new Date(System.currentTimeMillis() - TEST_EXPIRATION * 1000));
        // when
        boolean isValid = tokenProvider.validateAccessToken(expiredAccessToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("refreshToken의 유효성을 검증한다")
    void validateRefreshToken() {
        //given
        String refreshToken = tokenProvider.createRefreshToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date());

        //when
        boolean isValid = tokenProvider.validateRefreshToken(refreshToken);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 refreshToken을 검증한다")
    void validateExpiredRefreshToken() {
        // given
        String expiredRefreshToken = tokenProvider.createRefreshToken(TEST_SUBJECT, TEST_ROLE_STUDENT, new Date(System.currentTimeMillis() - TEST_EXPIRATION * 1000));

        // when
        boolean isValid = tokenProvider.validateAccessToken(expiredRefreshToken);

        // then
        assertThat(isValid).isFalse();
    }

    private Claims parseTokenSubject(String token, String secretKey) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}