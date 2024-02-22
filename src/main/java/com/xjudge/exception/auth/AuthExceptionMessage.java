package com.xjudge.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthExceptionMessage {
    private int code;

    private String message;

    private String timeStamp;

    private String desc;

    private Map<String, String> errors;
}
