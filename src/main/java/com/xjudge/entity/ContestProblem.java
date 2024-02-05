package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.key.ContestProblemKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contest_problem")
public class ContestProblem {

    @EmbeddedId
    ContestProblemKey id;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    @MapsId("contestId")
    private Contest contest;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "problem_id")
    @MapsId("problemId")
    private Problem problem;

    private int problemWeight;

    private String alias;

    private String code;

}
