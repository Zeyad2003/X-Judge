package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionModel;

import java.util.List;

public interface SubmissionService {
    SubmissionModel getSubmissionById(Long submissionId);

    Submission save(Submission submission);

    List<Submission> getSubmissionsByContestId(Long contestId);

    List<Submission> getSubmissionsByContestIdAndUserId(Long contestId , Long userId);

    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);
}
