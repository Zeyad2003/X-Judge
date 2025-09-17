package com.xjudge.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiBaseException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, cause);
    }

}
