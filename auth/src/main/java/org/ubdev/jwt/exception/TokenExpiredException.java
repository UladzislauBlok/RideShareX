package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends BaseJwtException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public TokenExpiredException(String s) {
        super(s, STATUS);
    }
}
