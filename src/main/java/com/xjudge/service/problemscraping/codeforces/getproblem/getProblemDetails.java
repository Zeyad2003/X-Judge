package com.xjudge.service.problemscraping.codeforces.getproblem;

import com.xjudge.entity.Problem;

public interface getProblemDetails {
    Problem Details(int contestId, char letter);
}
