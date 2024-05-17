package com.xjudge.service.contest.contestproblem;

import com.xjudge.entity.ContestProblem;

public interface ContestProblemService {
    ContestProblem getContestProblemByContestIdAndProblemId(Long contestId , Long problemId);
}
