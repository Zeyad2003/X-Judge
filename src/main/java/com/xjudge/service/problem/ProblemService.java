package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;

public interface ProblemService {

    Problem getProblemById(Long problemId);

    Problem getProblemByCode(String problemCode);

    Submission submit(SubmissionInfoModel info);
}
