package org.ubdev.jwt.model;

public record TokenResponse(String accessToken, String accessTokenExpiry,
                            String refreshToken, String refreshTokenExpiry) {
}
