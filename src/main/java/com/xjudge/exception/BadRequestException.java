package com.xjudge.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiBaseException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
