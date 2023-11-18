package com.xjudge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * <strong>User Entity</strong>
 * <p>User entity is used to store all user information</p>
 */
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

  private String userPassword;

  @Column(unique = true) private String userHandle;

  private String userFirstName;

  private String userLastName;

  @Column(unique = true) private String userEmail;

  private String userSchool;

  @Column(columnDefinition = "DATE") private LocalDate userRegistrationDate;

  private String userPhotoUrl;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "statistics_id")
  private UserStatistics userStatistics;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  List<Submission> userSubmission;

  @ManyToMany
  @JoinTable(name = "favorite_problems",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "problem_id"))
  private List<Problem> userFavoriteProblems;

  @ManyToMany(mappedBy = "contestUsers", fetch = FetchType.EAGER)
  private List<Contest> userContests;

  @ManyToMany(mappedBy = "groupUsers", fetch = FetchType.EAGER)
  private List<Group> userGroups;
}
