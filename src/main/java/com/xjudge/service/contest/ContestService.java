package com.xjudge.service.contest;

import com.xjudge.entity.Contest;
import com.xjudge.model.contest.modification.ContestModificationModel;
import com.xjudge.model.contest.modification.ContestCreationModel;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContestService {
    Page<Contest> getAllContests(Pageable pageable);

    Contest createContest(ContestCreationModel creationModel);

    Contest getContest(Long id);

    Contest updateContest(Long id, ContestModificationModel model);

    void deleteContest(Long id);

    List<ProblemModel> getContestProblems(Long id);

    SubmissionModel submitInContest(Long id, SubmissionInfoModel info);

    List<SubmissionModel> getContestSubmissions(Long id);
}
