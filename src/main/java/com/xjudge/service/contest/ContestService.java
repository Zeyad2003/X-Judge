package com.xjudge.service.contest;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Problem;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestUpdatingModel;
import com.xjudge.model.problem.ProblemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ContestService {
    Page<Contest> getAllContests(Pageable pageable);

    Contest createContest(ContestCreationModel creationModel);

    Contest getContest(Long id);

    Contest updateContest(Long id, ContestUpdatingModel model);

    void deleteContest(Long id);

    List<ProblemModel> getContestProblems(Long id);
}
