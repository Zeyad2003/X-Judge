package com.xjudge.model.auth;

import lombok.*;

@Data
@Builder
public class ForgotPasswordResponse {
    private int statusCode;
    private String message;
}
