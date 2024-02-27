package com.xjudge.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long id;

    private String handel;

    private String firstName;

    private String lastName;

    private String email;

    private String school;

    private LocalDate registrationDate;

    private String photoUrl;

}
