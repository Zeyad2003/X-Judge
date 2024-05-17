package com.xjudge.service.contest.contestproblem;

import com.xjudge.entity.ContestProblem;
import com.xjudge.repository.ContestProblemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContestProblemServiceImp implements ContestProblemService {
    private ContestProblemRepo repo;

    @Override
    public ContestProblem getContestProblemByContestIdAndProblemId(Long contestId, Long problemId) {
        return repo.findContestProblemByContestIdAndProblemId(contestId, problemId);
    }
}
