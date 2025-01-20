package com.example.lms.common.auth.jwt;

public class TokenConstants {
    public static final String AUTHORITIES_KEY = "roles";
    public static final String REDIS_PREFIX_REFRESH = ":RT:";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
}
