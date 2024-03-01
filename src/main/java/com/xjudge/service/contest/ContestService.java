package com.xjudge.service.contest;

import com.xjudge.entity.Contest;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ContestService {
    Page<Contest> getAllContests(Pageable pageable);

    Contest createContest(ContestClientRequest creationModel , Authentication authentication);

    Contest getContest(Long id);

    Contest updateContest(Long id, ContestClientRequest model , Authentication authentication);

    void deleteContest(Long id);

    List<ProblemModel> getContestProblems(Long id);

    ProblemModel getContestProblem(Long id, String problemHashtag);

    SubmissionModel submitInContest(Long id, SubmissionInfoModel info);

    List<SubmissionModel> getContestSubmissions(Long id);
}
