package com.xjudge.exception;

import org.springframework.http.HttpStatus;

public class NotImplementedException extends ApiBaseException {

    public NotImplementedException(String message) {
        super(message, HttpStatus.NOT_IMPLEMENTED);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_IMPLEMENTED, cause);
    }
}
