package com.xjudge.exception.handler;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.xjudge.exception.ApiBaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(ApiBaseException.class)
    public ResponseEntity<ErrorResponse> handleApiBaseException(ApiBaseException ex) {
        log.error("API exception occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

}
