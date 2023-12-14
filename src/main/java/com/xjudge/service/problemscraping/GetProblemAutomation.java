package com.xjudge.service.problemscraping;

import com.xjudge.entity.Problem;

public interface GetProblemAutomation {

    Problem GetProblem(int contestId, char letter);

}
