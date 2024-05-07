package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAuthoritiesException extends BaseJwtException {
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public UnauthorizedAuthoritiesException(String s) {
        super(s, STATUS);
    }
}
