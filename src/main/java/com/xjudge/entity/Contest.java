package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="contest")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contest extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contest title can't be empty.")
    @Column(nullable = false)
    private String title;

    @Future(message = "The contest begin time must be in the future.")
    @Column(nullable = false)
    private Instant beginTime;

    @DurationMax(days = 365, message = "The contest length must be less than 1 year.")
    @Column(nullable = false)
    private Duration duration;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContestVisibility visibility;

    private String password;

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<UserContest> users = new HashSet<>();

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<ContestProblem> problemSet = new HashSet<>();

}