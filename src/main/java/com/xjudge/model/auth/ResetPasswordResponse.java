package com.xjudge.model.auth;

import lombok.*;

@Data
@Builder
public class ResetPasswordResponse {
    private int statusCode;
    private String message;
}
