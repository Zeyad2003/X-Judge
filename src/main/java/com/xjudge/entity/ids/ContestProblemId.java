package com.xjudge.entity.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblemId implements Serializable {

    private Long contest;
    private Long problem;
}
