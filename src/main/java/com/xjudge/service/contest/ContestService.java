package com.xjudge.service.contest;

import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestData;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.problem.ContestProblemResp;

import java.util.List;

public interface ContestService {
    ContestData createContest(ContestCreationModel contest);
    List<ContestData> getAllContests();

    ContestData getContest(Long id);
    ContestModel updateContest(Long id , ContestModel model);
    void deleteContest(Long id);

    List<ContestProblemResp> gerContestProblems(Long id);
}
