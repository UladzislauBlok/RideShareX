package org.ubdev.jwt.exception;

public class UnauthorizedAuthoritiesException extends IllegalArgumentException {
    public UnauthorizedAuthoritiesException(String s) {
        super(s);
    }
}
