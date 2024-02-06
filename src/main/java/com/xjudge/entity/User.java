package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.model.enums.GroupRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
@Table(name = "user")
public class User extends BaseEntity<Long> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    GroupRole role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    @ToString.Exclude
    private UserStatistics userStatistics;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    List<Submission> userSubmission;

    @ManyToMany
    @JoinTable(
            name = "favorite_problems",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    @ToString.Exclude
    private List<Problem> userFavoriteProblems;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<UserContest> contests = new HashSet<>();

//======================================================================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userHandle;
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
