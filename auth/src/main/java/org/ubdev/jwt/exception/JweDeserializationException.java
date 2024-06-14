package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;

public class JweDeserializationException extends BaseJwtException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public JweDeserializationException(String s) {
        super(s, STATUS);
    }
}
