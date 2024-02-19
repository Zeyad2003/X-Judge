package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.key.ContestProblemKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contest_problem")
public class ContestProblem extends BaseEntity<ContestProblemKey> {

    @EmbeddedId
    ContestProblemKey id;

    @ManyToOne
    @JsonIgnore
    @MapsId("contestId")
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne
    @JsonIgnore
    @MapsId("problemId")
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private Integer problemWeight;

    private String problemAlias;

    private String problemCode;

    private Integer problemAccepted;

    private Integer problemAttempted;

}