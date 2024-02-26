package com.xjudge.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestProblemKey implements Serializable {

    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "problem_id")
    private Long problemId;
}
