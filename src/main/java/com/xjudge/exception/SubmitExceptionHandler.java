package com.xjudge.exception;

import com.github.dockerjava.api.exception.BadRequestException;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.exception.auth.AuthExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class SubmitExceptionHandler {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    @ExceptionHandler(SubmitException.class)
    public ResponseEntity<?> submitException(SubmitException exception, WebRequest webRequest) {
        ExceptionMessage errorDetails = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException exception, WebRequest webRequest) {
        AuthExceptionMessage errorDetails = new AuthExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false),
                exception.getErrors()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> submitException(BadRequestException exception, WebRequest webRequest) {
        ExceptionMessage errorDetails = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(Exception exception, WebRequest webRequest) {
        ExceptionMessage errorDetails = new ExceptionMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
