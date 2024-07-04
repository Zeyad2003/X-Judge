package com.xjudge.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class XJudgeValidationException extends RuntimeException{

    private final Map<String , String> errors;
    private final String location;
    private final HttpStatus httpStatus;
    private final String message;
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    public XJudgeValidationException(Map<String, String> errors, String message, String location, HttpStatus status) {
        super(message);
        this.location = location;
        this.httpStatus = status;
        this.message = message;
        this.errors = errors;
    }

    public XJudgeValidationException(List<FieldError> fieldErrors ,String message ,  String location , HttpStatus status){
        super(message);
        this.location = location;
        this.httpStatus = status;
        this.message = message;
        errors = new HashMap<>();
        for(FieldError fieldError : fieldErrors){
            errors.put(fieldError.getField() , fieldError.getDefaultMessage());
        }

    }

}
