package org.ubdev.user.exception.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.ubdev.user.exception.BaseUserException;
import org.ubdev.user.exception.response.ErrorResponse;

import static java.lang.Thread.currentThread;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {
    private static final String LOG_MESSAGE_PATTERN = "Exception %s was thrown in thread %s with message %s";

    @ExceptionHandler(BaseUserException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(BaseUserException ex, HttpServletRequest request) {
        log.debug(LOG_MESSAGE_PATTERN.formatted(ex.getCause(), currentThread().toString(), ex.getMessage()));
        return buildResponse(ex.getMessage(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServletException ex, HttpServletRequest request) {
        log.debug(LOG_MESSAGE_PATTERN.formatted(ex.getCause(), currentThread().toString(), ex.getMessage()));
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus statusCode, HttpServletRequest request) {
        var errorResponse = new ErrorResponse(message, statusCode, request.getServletPath());
        return new ResponseEntity<>(errorResponse, statusCode);
    }
}
