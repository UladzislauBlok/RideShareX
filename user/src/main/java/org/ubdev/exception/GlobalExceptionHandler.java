package org.ubdev.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.lang.Thread.currentThread;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String EXCEPTION_LOGGING_PATTERN = "Exception '%s' was thrown in thread '%s' with messages: %s";

    @Order(Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        log.info(EXCEPTION_LOGGING_PATTERN.formatted(
                e.getClass().getName(),
                currentThread().getName(),
                e.getMessage())
        );

        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        log.info(EXCEPTION_LOGGING_PATTERN.formatted(
                e.getClass().getName(),
                currentThread().getName(),
                e.getMessage())
        );
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleConverterErrors(RuntimeException e) {

        log.info(EXCEPTION_LOGGING_PATTERN.formatted(
                e.getClass().getName(),
                currentThread().getName(),
                e.getMessage())
        );
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
