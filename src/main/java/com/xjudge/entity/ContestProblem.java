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
    @MapsId("contestId")
    private Contest contest;

    @JsonIgnore
    @ManyToOne
    @MapsId("problemId")
    private Problem problem;

    private int problemWeight;

    private String alias;

    private String code;

}