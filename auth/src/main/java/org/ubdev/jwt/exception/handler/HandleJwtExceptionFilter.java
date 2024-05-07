package org.ubdev.jwt.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ubdev.jwt.exception.BaseJwtException;
import org.ubdev.jwt.exception.response.ErrorResponse;

import java.io.IOException;

import static java.lang.Thread.currentThread;

@Slf4j
@RequiredArgsConstructor
public class HandleJwtExceptionFilter extends OncePerRequestFilter {
    private static final String LOG_MESSAGE_PATTERN = "Exception %s was thrown in thread %s with message %s";
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseJwtException ex) {
            log.debug(LOG_MESSAGE_PATTERN.formatted(ex.getCause(), currentThread().toString(), ex.getMessage()));
            response.setStatus(ex.getHttpStatus().value());
            response.getWriter().write(buildResponseString(ex.getMessage(), ex.getHttpStatus(), request.getServletPath()));
        }
    }

    private String buildResponseString(String message, HttpStatus httpStatus, String contextPath) throws JsonProcessingException {
        var errorResponse = new ErrorResponse(message, httpStatus, contextPath);
        return objectMapper.writeValueAsString(errorResponse);
    }
}
