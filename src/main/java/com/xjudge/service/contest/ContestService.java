package com.xjudge.service.contest;

import com.xjudge.entity.Contest;
import com.xjudge.entity.User;
import com.xjudge.entity.UserContest;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.ContestRankModel;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ContestService {
    Page<ContestPageModel>  getAllContests(Pageable pageable);

    ContestModel createContest(ContestClientRequest creationModel , Authentication authentication);

    Contest getContest(Long id);

    ContestModel getContestData(Long id);

    ContestModel updateContest(Long id, ContestClientRequest model , Authentication authentication);

    void deleteContest(Long id);

    List<ProblemModel> getContestProblems(Long id);

    ProblemModel getContestProblem(Long id, String problemHashtag);

    SubmissionModel submitInContest(Long id, SubmissionInfoModel info , Authentication authentication);

    List<SubmissionModel> getContestSubmissions(Long id);
    UserContest handleContestUserRelation(User user, Contest contest , boolean isPOwner , boolean isParticipant);

    List<ContestRankModel> getRank(Long contestId);
}
