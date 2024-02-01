package com.xjudge.model.auth;

import com.xjudge.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "user password is mandatory")
    @Size(min = 4, message = "Password must be at least 8 characters long")
    private String userPassword;

    @NotBlank(message = "user handle is mandatory")
    private String userHandle;

    @NotBlank(message = "user first name  is mandatory")
    private String userFirstName;

    @NotBlank(message = "user last name  is mandatory")
    private String userLastName;

    @NotBlank(message = "user email is mandatory")
    @Email(message = "please enter valid email")
    private String userEmail;

    private String userSchool;

    private String userPhotoUrl;

    private UserRole role;

}
