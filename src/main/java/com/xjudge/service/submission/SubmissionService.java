package com.xjudge.service.submission;

import com.xjudge.entity.Submission;

import java.util.List;

public interface SubmissionService {
    Submission getSubmissionById(Long submissionId);

    Submission save(Submission submission);

    List<Submission> getSubmissionsByContestId(Long contestId);

}
