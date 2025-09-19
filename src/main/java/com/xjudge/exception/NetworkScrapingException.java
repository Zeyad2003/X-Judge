package com.xjudge.exception;

import org.springframework.http.HttpStatus;

public class NetworkScrapingException extends ApiBaseException {


    public NetworkScrapingException(String message, HttpStatus status) {
        super(message, status);
    }

    public NetworkScrapingException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY, cause);
    }


}
