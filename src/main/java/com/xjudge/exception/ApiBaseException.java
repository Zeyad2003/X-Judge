package com.xjudge.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Base exception for all API exceptions.
 */

@Getter
public abstract class ApiBaseException extends RuntimeException {
    private final HttpStatus status;

    public ApiBaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiBaseException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
