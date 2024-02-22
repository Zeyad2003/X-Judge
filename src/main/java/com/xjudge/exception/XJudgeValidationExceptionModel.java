package com.xjudge.exception;

import lombok.*;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XJudgeValidationExceptionModel extends ExceptionModel{
    Map<String , String> errors;
    public XJudgeValidationExceptionModel(int statusCode, String message, String location, String timeStamp, String description, Map<String, String> errors) {
        super(statusCode, message, location, timeStamp, description);
        this.errors = errors;
    }
}
