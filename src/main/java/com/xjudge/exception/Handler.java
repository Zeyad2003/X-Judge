package com.xjudge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class Handler {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    @ExceptionHandler(XJudgeException.class)
    public ResponseEntity<?> submitException(XJudgeException exception, WebRequest webRequest) {
        return createResponseEntity(exception, exception.getLocation(), exception.getStatusCode(), webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> runtimeException(Exception exception, String location, WebRequest webRequest) {
        return createResponseEntity(exception, location, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    private ResponseEntity<?> createResponseEntity(Exception exception, String location, HttpStatus status, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                status.value(),
                exception.getMessage(),
                location,
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, status);
    }
}
