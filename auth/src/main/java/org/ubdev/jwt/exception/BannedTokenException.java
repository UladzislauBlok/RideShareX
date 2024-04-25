package org.ubdev.jwt.exception;

public class BannedTokenException extends IllegalArgumentException {
    public BannedTokenException(String s) {
        super(s);
    }
}
