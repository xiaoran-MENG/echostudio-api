package com.echostudio.constant;

public final class SecurityConstants {
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String DEFAULT_ROLE = "USER";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String DEFAULT_ADMIN_USER_EMAIL = "admin@echostudio.com";
    public static final String DEFAULT_ADMIN_USER_PASSWORD = "admin123";
    public static final String[] REQUEST_PATHS_PERMITTED = {
        "/api/auth/login",
        "/api/auth/register",
        "/api/health"
    };
    public static final String[] PUBLIC_REQUEST_PATHS = {
        "/api/albums",
        "/api/songs"
    };
}
