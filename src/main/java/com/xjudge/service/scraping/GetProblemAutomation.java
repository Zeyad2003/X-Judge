package com.xjudge.service.scraping;

import com.xjudge.entity.Problem;

public interface GetProblemAutomation {

    Problem GetProblem(int contestId, char letter);

}
