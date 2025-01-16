package com.example.lms.common.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisService redisService;

    private static final String REDIS_KEY = "RT:1";
    private static final String REFRESH_TOKEN = "testRefreshToken";
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 1000L * 60 * 60 * 24;

    @Test
    @DisplayName("RefreshToken을 Redis에 저장한다")
    void saveRefreshToken() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        redisService.saveRefreshToken(REDIS_KEY, REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION_SECONDS);

        // then
        verify(valueOperations, times(1))
                .set(REDIS_KEY, REFRESH_TOKEN, REFRESH_TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Key 에 해당하는 RefreshToken을 Redis에서_조회한다")
    void findRefreshToken() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(REDIS_KEY)).thenReturn(REFRESH_TOKEN);

        // when
        String resultRefreshToken = redisService.getRefreshToken(REDIS_KEY);

        // then
        assertThat(resultRefreshToken).isEqualTo(REFRESH_TOKEN);
        verify(valueOperations, times(1)).get(REDIS_KEY);
    }

    @Test
    @DisplayName("존재하지 않는 Key로 RefreshToken을 Redis에서 조회시 null을 반환한다")
    void findRefreshTokenNullKey() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(REDIS_KEY)).thenReturn(null);

        // when
        String resultRefreshToken = redisService.getRefreshToken(REDIS_KEY);

        // then
        assertThat(resultRefreshToken).isNull();
        verify(valueOperations, times(1)).get(REDIS_KEY);
    }

    @Test
    @DisplayName("RefreshToken을 Redis에서 삭제한다")
    void deleteRefreshToken() {
        // given
        when(redisTemplate.delete(REDIS_KEY)).thenReturn(true);

        // when
        Boolean result = redisService.deleteRefreshToken(REDIS_KEY);

        // then
        assertThat(result).isTrue();
        verify(redisTemplate, times(1)).delete(REDIS_KEY);
    }

    @Test
    @DisplayName("Redis에서_RefreshToken_삭제에_실패한다")
    void deleteRefreshTokenFailed() {
        // given
        when(redisTemplate.delete(REDIS_KEY)).thenReturn(false);

        // when
        Boolean result = redisService.deleteRefreshToken(REDIS_KEY);

        // then
        assertThat(result).isFalse();
        verify(redisTemplate, times(1)).delete(REDIS_KEY);
    }
}