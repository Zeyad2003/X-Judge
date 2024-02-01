package com.xjudge.service.contest;

import com.xjudge.entity.Problem;
import com.xjudge.model.contest.ContestCreationRequest;
import com.xjudge.model.contest.ContestDataResp;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestProblemData;
import com.xjudge.model.problem.ContestProblemResp;
import org.springframework.cache.interceptor.CacheInterceptor;

import java.util.List;

public interface ContestService {
    ContestDataResp createContest(Long userId , ContestCreationRequest contest);
    List<ContestDataResp> getAllContests();

    ContestDataResp getContest(Long id);
    ContestModel updateContest(Long id , ContestModel model);
    void deleteContest(Long id);

    List<ContestProblemResp> gerContestProblems(Long id);
}
