package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;

public class BannedTokenException extends BaseJwtException {
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public BannedTokenException(String s) {
        super(s, STATUS);
    }
}
