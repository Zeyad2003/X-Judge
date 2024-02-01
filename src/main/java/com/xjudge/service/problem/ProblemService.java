package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ContestProblemResp;

public interface ProblemService {
    ContestProblemResp getProblemDataForContest(String problemCode);

    Problem getProblem(Long id);
}
