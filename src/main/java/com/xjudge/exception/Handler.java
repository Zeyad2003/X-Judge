package com.xjudge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        return createResponseEntity(exception, exception.getClass().getName(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(XJudgeValidationException.class)
    public ResponseEntity<?> xJudgeValidationException(XJudgeValidationException exception , WebRequest webRequest){
        ExceptionModel errorDetails = new XJudgeValidationExceptionModel(
                exception.getHttpStatus().value(),
                exception.getMessage() ,
                exception.getLocation(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false) ,
                exception.getErrors()
        );
        return new ResponseEntity<>(errorDetails , exception.getHttpStatus());
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
