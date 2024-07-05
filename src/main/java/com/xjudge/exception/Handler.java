package com.xjudge.exception;

import com.xjudge.exception.auth.AuthException;
import com.xjudge.exception.auth.AuthExceptionMessage;
import com.xjudge.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class Handler {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    @ExceptionHandler(XJudgeException.class)
    public ResponseEntity<?> submitException(XJudgeException exception, WebRequest webRequest) {
        return createResponseEntity(exception, exception.getLocation(), exception.getStatusCode(), webRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        XJudgeValidationExceptionModel errorDetails = new XJudgeValidationExceptionModel(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                this.getClass().getName(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false),
                errors
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException exception, WebRequest webRequest) {
        AuthExceptionMessage errorDetails = new AuthExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false),
                exception.getErrors()
        );
        Response errorResponse = Response.builder()
                .success(false)
                .error(errorDetails)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request body",
                this.getClass().getName(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                HttpStatus.BAD_REQUEST.value(),
                String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()),
                this.getClass().getName(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                this.getClass().getName(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                HttpStatus.NOT_FOUND.value(),
                "The requested resource was not found",
                this.getClass().getName(),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> createResponseEntity(Exception exception, String location, HttpStatus status, WebRequest webRequest) {
        ExceptionModel errorDetails = new ExceptionModel(
                status.value(),
                exception.getMessage(),
                location,
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now()),
                webRequest.getDescription(false)
        );
        Response errorResponse = Response.builder()
                .success(false)
                .error(errorDetails)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}
