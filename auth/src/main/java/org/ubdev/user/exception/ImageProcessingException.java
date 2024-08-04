package org.ubdev.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ImageProcessingException extends RuntimeException {
    private static final HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public ImageProcessingException(String s) {
        super(s);
    }
}
