package com.xjudge.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.UserContest;
import com.xjudge.entity.UserGroup;
import com.xjudge.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long id;

    @Size(max = 20 , message = "user handle length must be less than 20")
    private String handle;

    @NotBlank(message = "user first name  is mandatory")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "user last name  is mandatory")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "user email is mandatory")
    @Email(message = "please enter valid email")
    private String email;

    @Size(max = 100, message = "user school length must be less than 7")
    private String school;

    private LocalDate registrationDate;

    @URL(message = "Invalid URL")
    private String photoUrl;

    private Boolean isVerified;

    private Long solvedCount;

    private Long attemptedCount;

    @JsonIgnore
    private UserRole role;

    @JsonIgnore
    Set<Submission> submissions;

    @JsonIgnore
    private Set<Problem> favoriteProblems;

    @JsonIgnore
    private Set<UserContest> contests = new HashSet<>();

    @JsonIgnore
    private List<UserGroup> groups;

}
