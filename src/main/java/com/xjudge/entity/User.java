package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <strong>User Entity</strong>
 * <p>User entity is used to store all user information</p>
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String userPassword;

    @Column(unique = true)
    private String userHandle;

    private String userFirstName;

    private String userLastName;

    @Column(unique = true)
    private String userEmail;

    private String userSchool;

    @Column(columnDefinition = "DATE")
    private LocalDate userRegistrationDate;

    private String userPhotoUrl;

    @Enumerated(EnumType.STRING)
    UserRole role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private UserStatistics userStatistics;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    List<Submission> userSubmission;

    @ManyToMany
    @JoinTable(
            name = "favorite_problems",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    private List<Problem> userFavoriteProblems;

    @ManyToMany(mappedBy = "contestUsers", fetch = FetchType.LAZY)
    private List<Contest> userContests;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGroup> groups;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userHandle;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
