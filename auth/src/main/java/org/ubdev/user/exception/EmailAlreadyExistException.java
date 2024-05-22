package org.ubdev.user.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends BaseUserException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public EmailAlreadyExistException(String s) {
        super(s, STATUS);
    }
}
