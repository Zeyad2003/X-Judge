package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;

public interface ProblemService {
    ProblemModel getProblem(String problemCode);

    SubmissionResult submit(SubmissionInfo info);
}
