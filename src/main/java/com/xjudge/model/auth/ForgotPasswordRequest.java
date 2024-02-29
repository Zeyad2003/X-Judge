package com.xjudge.model.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(message = "Should be on form -example@email.com-")
    private String email;
}
