package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BannedTokenException extends IllegalArgumentException {
    public BannedTokenException(String s) {
        super(s);
    }
}
