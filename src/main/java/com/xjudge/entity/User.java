package com.xjudge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_handle", unique = true)
    private String userHandle;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_last_name")
    private String userLastName;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_school")
    private String userSchool;

    @Column(name = "user_registration_date", columnDefinition = "DATE")
    private LocalDate userRegistrationDate;

    @Column(name = "user_photo_url")
    private String userPhotoUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_statistics_id")
    private Statistics statistics;

    @ManyToMany
    @JoinTable(
            name = "favorite_problems",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    private List<Problem> favoriteProblems;

    @OneToMany(mappedBy = "user")
    private List<Submission> submissions;

//    @ManyToMany(mappedBy = "users")
//    private List<Group> groups;
}
