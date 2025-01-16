package com.example.lms.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;


    public void saveRefreshToken(String key, String refreshToken, long refreshTokenExpirationSeconds) {
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenExpirationSeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean deleteRefreshToken(String key) {
        return redisTemplate.delete(key);
    }
}
