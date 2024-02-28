package com.xjudge.model.auth;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private int statusCode;
    private String message;
}
