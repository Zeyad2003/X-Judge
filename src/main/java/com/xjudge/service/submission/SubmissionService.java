package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;

import java.util.List;

public interface SubmissionService {
    Submission getSubmissionById(Long submissionId);

    Submission save(Submission submission);

    List<Submission> getSubmissionsByContestId(Long contestId);

    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);
}
