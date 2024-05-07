package org.ubdev.security.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        HttpStatus status_code,
        String path,
        LocalDateTime timestamp) {

    public ErrorResponse(String message, HttpStatus status_code, String path) {
        this(message, status_code, path, LocalDateTime.now());
    }
}
