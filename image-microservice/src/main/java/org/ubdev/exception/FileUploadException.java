package org.ubdev.exception;

import org.springframework.http.HttpStatus;

public class FileUploadException extends BaseFileException {

    public FileUploadException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}