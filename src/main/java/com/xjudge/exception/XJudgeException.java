package com.xjudge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class XJudgeException extends RuntimeException{
    private final HttpStatus statusCode;

    public XJudgeException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

