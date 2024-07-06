package com.xjudge.service.submission;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.User;
import com.xjudge.model.contest.ContestStatusPageModel;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SubmissionService {
    SubmissionModel getSubmissionById(Long submissionId , Authentication authentication);

    Page<SubmissionPageModel> getAllSubmissions(Pageable pageable);

    Submission save(Submission submission);

    boolean updateSubmissionOpen(Long submissionId , Authentication authentication);

    List<Submission> getSubmissionsByContestId(Long contestId);

    List<Submission> getSubmissionsByContestIdAndUserId(Long contestId , Long userId);

    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);

    Page<SubmissionPageModel> filterSubmissions(String userHandle, String oj, String problemCode, String language, Pageable pageable);

    Page<ContestStatusPageModel> filterSubmissionsInContest(Long contestId , String userHandle, String problemCode, String result ,String language, Pageable pageable);

    List<Submission> findByUserAndProblem(User user, Problem problem);

    List<Submission> getSubmissionByStatus(String status);
}
