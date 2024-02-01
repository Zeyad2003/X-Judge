package com.xjudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xjudge.entity.ids.ContestProblemId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = ContestProblemId.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contest")
public class ContestProblem {


    @Id
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @JsonIgnore
    @Id
    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private int problemWeight;

    private String alias;

    private String code;

}
