package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProblemService {
    Page<Problem> getAllProblems(Pageable pageable);

    Problem getProblemById(Long problemId);

    Problem getProblemByCode(String problemCode);

    Submission submit(SubmissionInfoModel info);

}
