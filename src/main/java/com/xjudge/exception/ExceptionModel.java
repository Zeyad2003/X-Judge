package com.xjudge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionModel {
    private int statusCode;

    private String message;

    private String location;

    private String timeStamp;

    private String description;
}
