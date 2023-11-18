package com.xjudge.entity;

import com.xjudge.enums.ContestType;
import com.xjudge.enums.ContestVisibility;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <strong>Contest Entity</strong>
 * <p>Represents a contest with a list of problems and submissions.</p>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contest")
public class Contest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long contestId;

  private String contestTitle;

  private Instant contestBeginTime;

  private Duration contestLength;

  @Column(columnDefinition = "TEXT") private String contestDescription;

  @Enumerated(EnumType.STRING) private ContestType contestType;

  @Enumerated(EnumType.STRING) private ContestVisibility contestVisibility;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "contest_id")
  private List<Submission> contestSubmissions;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_contest",
             joinColumns = @JoinColumn(name = "contest_id"),
             inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> contestUsers;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "contest_problem",
             joinColumns = @JoinColumn(name = "contest_id"),
             inverseJoinColumns = @JoinColumn(name = "problem_id"))
  private List<Problem> problems;
}
