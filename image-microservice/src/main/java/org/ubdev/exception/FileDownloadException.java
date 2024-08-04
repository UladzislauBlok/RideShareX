package org.ubdev.exception;

import org.springframework.http.HttpStatus;

public class FileDownloadException extends BaseFileException {

    public FileDownloadException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
