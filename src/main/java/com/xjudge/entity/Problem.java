package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.util.JsonDataConverter;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Problem extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String problemCode;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String statement;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String input;

    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String output;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private OnlineJudgeType source;

    private String problemLink;

    private String contestLink;

    private String contestName;

    private String timeLimit;

    private String memoryLimit;

    @Builder.Default
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Sample> samples = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "problem",fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @ToString.Exclude
    Set<Submission> submissions;

    @JsonIgnore
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<ContestProblem> contests ;

    @JsonIgnore
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<UserProblem> userProblems;

    @Convert(converter = JsonDataConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> extraInfo;

}
