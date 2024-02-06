package com.xjudge.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum OnlineJudgeType {
    CODEFORCES("CodeForces");

    private final String name;
}
