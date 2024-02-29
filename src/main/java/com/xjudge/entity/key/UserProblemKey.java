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
public class UserProblemKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "problem_id")
    private Long problemId;
}
