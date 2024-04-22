package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenExpiredException extends IllegalStateException {
    public TokenExpiredException(String s) {
        super(s);
    }
}
