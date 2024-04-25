package org.ubdev.jwt.exception;

public class IncorrectTokenTypeException extends IllegalArgumentException {
    public IncorrectTokenTypeException(String s) {
        super(s);
    }
}
