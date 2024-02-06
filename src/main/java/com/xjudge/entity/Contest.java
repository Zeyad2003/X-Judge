package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.persistence.*;
import lombok.*;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
public class Contest extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contestTitle;

    private Instant contestBeginTime;

    private Duration contestLength;

    @Column(columnDefinition = "TEXT")
    private String contestDescription;

    @Enumerated(EnumType.STRING)
    private ContestType contestType;

    @Enumerated(EnumType.STRING)
    private ContestVisibility contestVisibility;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contest_id")
    @ToString.Exclude
    private List<Submission> contestSubmissions = new ArrayList<>();

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<UserContest> contestUsers = new HashSet<>();

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<ContestProblem> problemSet = new HashSet<>();

}