package com.xjudge.entity;

import com.xjudge.util.JsonDataConverter;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="submission")
public class Submission extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remoteRunId;

    private OnlineJudgeType ojType;

    @Column(columnDefinition = "LONGTEXT")
    @Size(min = 20, max = 65535)
    private String solution;

    private String language;

    private Instant submitTime;

    private String memoryUsage;

    private String timeUsage;

    private String verdict;

    private Boolean isOpen;

    private String submissionStatus; // kept updated (submitted, in queue, running test 14, Accepted)

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @ToString.Exclude
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    @ToString.Exclude
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Convert(converter = JsonDataConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> extraInfo = new HashMap<>(); // Any extra info like contest id, etc.

}
