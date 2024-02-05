package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="contest")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contestId")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contestId;

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
    private List<Submission> contestSubmissions = new ArrayList<>();

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    private Set<UserContest> contestUsers = new HashSet<>();

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
    Set<ContestProblem> problemSet = new HashSet<>();

}
