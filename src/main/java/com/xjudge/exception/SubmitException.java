package com.xjudge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SubmitException extends RuntimeException{
    private final HttpStatus statusCode;
    public SubmitException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

