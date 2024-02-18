package com.xjudge.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Please enter a valid handle")
    private String userHandle;

    @NotBlank(message = "Please enter a valid password")
    private String userPassword;

}
