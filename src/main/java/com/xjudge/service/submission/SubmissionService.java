package com.xjudge.service.submission;

import com.xjudge.entity.Submission;

public interface SubmissionService {
    Submission getSubmissionById(Long submissionId);

    Submission save(Submission submission);

}
