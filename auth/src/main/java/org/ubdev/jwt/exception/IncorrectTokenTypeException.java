package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;

public class IncorrectTokenTypeException extends BaseJwtException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public IncorrectTokenTypeException(String s) {
        super(s, STATUS);
    }
}
