package com.xjudge.service.scraping;

import com.xjudge.entity.Problem;

import java.io.IOException;

public interface GetProblemAutomation {

    Problem GetProblem(int contestId, char letter);

}
