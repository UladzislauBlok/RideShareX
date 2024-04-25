package org.ubdev.jwt.exception;

public class TokenExpiredException extends IllegalStateException {
    public TokenExpiredException(String s) {
        super(s);
    }
}
