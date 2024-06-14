package org.ubdev.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseUserException extends IllegalArgumentException {
    private final HttpStatus statusCode;

    public BaseUserException(String s, HttpStatus statusCode) {
        super(s);
        this.statusCode = statusCode;
    }
}
