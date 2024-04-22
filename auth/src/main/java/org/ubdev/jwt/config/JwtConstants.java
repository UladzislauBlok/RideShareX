package org.ubdev.jwt.config;

public class JwtConstants {
    public static final Long RefreshTokenTtlInDays = 1L;
    public static final Integer AccessTokenTtlInMinutes = 5;
    public static final String AUTHORITIES = "authorities";
    public static final String GRANT_PREFIX = "GRANT_";
    public static final String JWT_REFRESH = "JWT_REFRESH";
    public static final String JWT_LOGOUT = "JWT_LOGOUT";
    public static final String ACCESS_DENIED_MESSAGE = "User must be authenticated";
    public static final String JWE_DESERIALIZATION_EXCEPTION_MESSAGE = "Failed to decrypt Jwe";
    public static final String INCORRECT_JWT_TOKEN_EXCEPTION_MESSAGE = "Incorrect token type";
    public static final String BANNED_TOKEN_EXCEPTION_MESSAGE = "This token is blocked";
    public static final String TOKEN_EXPIRED_EXCEPTION_MESSAGE = "The token has expired";
}
