package org.ubdev.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.ubdev.user.exception.exceptions.DuplicateEmailException;
import org.ubdev.user.exception.exceptions.UserNotFoundException;

import java.util.Map;

import static java.lang.Thread.currentThread;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {

    private static final String EXCEPTION_LOGGING_PATTERN = "Exception '%s' was thrown in thread '%s' with messages: %s";

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        log.info(EXCEPTION_LOGGING_PATTERN.formatted(
                e.getClass().getName(),
                currentThread().getName(),
                e.getMessage())
        );

        return new ResponseEntity<>(Map.of("message", "There is no user with this id"), HttpStatus.NOT_FOUND);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler({DuplicateEmailException.class})
    public ResponseEntity<?> handleValidationErrors(DuplicateEmailException e) {
        return new ResponseEntity<>(Map.of("message", "This email has already been used "), HttpStatus.CONFLICT);
    }
}
