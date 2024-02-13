package com.xjudge.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class AuthException extends RuntimeException {
    private final HttpStatus statusCode;
    private final Map<String, String> errors;
    public AuthException(String message, HttpStatus statusCode, Map<String, String> errors) {
        super(message);
        this.statusCode = statusCode;
        this.errors = errors;
    }
}
