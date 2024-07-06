package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="submission")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Submission extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remoteRunId;

    private OnlineJudgeType ojType;

    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 65535)
    private String solution;

    private String language;

    private Instant submitTime;

    private String memoryUsage;

    private String timeUsage;

    private String verdict;

    private Boolean isOpen;

    private String submissionStatus; // kept updated (submitted, in queue, running test 14, Accepted)

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contest_id")
    @ToString.Exclude
    private Contest contest;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    @ToString.Exclude
    private Problem problem;


    @ManyToOne(fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="compiler_id" , nullable = false)
    @ToString.Exclude
    private Compiler compiler;

}
