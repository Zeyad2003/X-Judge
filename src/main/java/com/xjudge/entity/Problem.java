package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private String code;
    private OnlineJudgeType onlineJudge;
    private String title;
    private String contestName;
    private String problemLink;
    private String contestLink;
    @Column(columnDefinition = "LONGTEXT")
    @Lob
    private String prependHtml;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Section> sections;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Property> properties;

    @JsonIgnore
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    Set<Submission> submissions;

    @JsonIgnore
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<ContestProblem> contests;

    @JsonIgnore
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<UserProblem> userProblems;

}
