package org.ubdev.user.exception;

public class EmailAlreadyExistException extends IllegalStateException {
    public EmailAlreadyExistException(String s) {
        super(s);
    }
}
