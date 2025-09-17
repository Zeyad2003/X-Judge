package com.xjudge.exception;

import org.springframework.http.HttpStatus;

public class ScrapingException extends ApiBaseException {


    public ScrapingException(String message, HttpStatus status) {
        super(message, status);
    }

    public ScrapingException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY, cause);
    }


}
