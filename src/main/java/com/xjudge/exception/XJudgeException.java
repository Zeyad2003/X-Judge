package com.xjudge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class XJudgeException extends RuntimeException{
    private final String location;
    private final HttpStatus statusCode;

    public XJudgeException(String message, String location, HttpStatus statusCode) {
        super(message);
        this.location = location;
        this.statusCode = statusCode;
    }
}

