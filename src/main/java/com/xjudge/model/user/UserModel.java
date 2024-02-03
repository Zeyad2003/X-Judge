package com.xjudge.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long userId;

    @JsonIgnore
    private String userPassword;

    private String userHandle;

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private String userSchool;

    private LocalDate userRegistrationDate;

    private String userPhotoUrl;

    @Enumerated(EnumType.STRING)
    UserRole role;
}
