package com.xjudge.model.auth;

import com.xjudge.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "user password is mandatory")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String userPassword;

    @NotBlank(message = "user handle is mandatory")
    @Size(max = 20 , message = "user handle length must be less than 20")
    private String userHandle;

    @NotBlank(message = "user first name  is mandatory")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String userFirstName;

    @NotBlank(message = "user last name  is mandatory")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String userLastName;

    @NotBlank(message = "user email is mandatory")
    @Email(message = "please enter valid email")
    private String userEmail;

    @Size(max = 7 , message = "user school length must be less than 7")
    private String userSchool;

    @URL(message = "Invalid URL")
    private String userPhotoUrl;
}
