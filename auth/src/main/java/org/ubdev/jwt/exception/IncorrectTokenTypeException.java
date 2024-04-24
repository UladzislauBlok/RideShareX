package org.ubdev.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectTokenTypeException extends IllegalArgumentException {
    public IncorrectTokenTypeException(String s) {
        super(s);
    }
}
