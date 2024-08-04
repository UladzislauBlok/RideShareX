package org.ubdev.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseFileException extends RuntimeException {
    private final HttpStatus statusCode;

    BaseFileException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
