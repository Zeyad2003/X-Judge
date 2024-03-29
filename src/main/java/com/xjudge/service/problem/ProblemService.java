package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.model.problem.ProblemsPageModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProblemService {
    Page<ProblemsPageModel> getAllProblems(Pageable pageable);

    Problem getProblemByCodeAndSource(String problemCode, String problemSource);

    Submission submit(SubmissionInfoModel info);

    Page<ProblemsPageModel> searchByTitle(String title, Pageable pageable);

    Page<ProblemsPageModel> searchBySource(String source, Pageable pageable);

    Page<ProblemsPageModel> searchByProblemCode(String problemCode, Pageable pageable);

}
