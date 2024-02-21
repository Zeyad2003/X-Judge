package com.xjudge.service.contest;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Problem;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestUpdatingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestService {
    Page<Contest> getAllContests(Pageable pageable);

    Contest createContest(ContestCreationModel creationModel);

    Contest getContest(Long id);

    Contest updateContest(Long id, ContestUpdatingModel model);

    void deleteContest(Long id);
  /*  List<ContestUpdatingModel> getAllContests();

    ContestUpdatingModel getContest(Long id);
    ContestUserDataModel updateContest(Long id , ContestUserDataModel model);
    void deleteContest(Long id);

    List<ContestProblemResp> gerContestProblems(Long id);*/
}
