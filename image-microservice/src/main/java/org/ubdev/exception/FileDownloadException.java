package org.ubdev.exception;


import lombok.Getter;

@Getter
public class FileDownloadException extends RuntimeException {
    private final int httpStatus;

    public FileDownloadException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
