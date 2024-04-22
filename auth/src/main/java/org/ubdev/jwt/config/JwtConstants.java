package org.ubdev.jwt.config;

public class JwtConstants {
    public static final Long RefreshTokenTtlInDays = 1L;
    public static final Integer AccessTokenTtlInMinutes = 5;
    public static final String AUTHORITIES = "authorities";
    public static final String GRANT_PREFIX = "GRANT_";
    public static final String JWT_REFRESH = "JWT_REFRESH";
    public static final String JWT_LOGOUT = "JWT_LOGOUT";
    public static final String ACCESS_DENIED_MESSAGE = "User must be authenticated";
}
