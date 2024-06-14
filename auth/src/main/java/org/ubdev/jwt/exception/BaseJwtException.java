package org.ubdev.jwt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseJwtException extends IllegalArgumentException {
    private final HttpStatus httpStatus;

    public BaseJwtException(String s, HttpStatus httpStatus) {
        super(s);
        this.httpStatus = httpStatus;
    }
}
