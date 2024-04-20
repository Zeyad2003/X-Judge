package com.xjudge.service.problem;

import com.xjudge.entity.Compiler;
import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemsPageModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProblemService {
    Page<ProblemsPageModel> getAllProblems(Pageable pageable);

    Problem getProblemByCodeAndSource(String problemCode, String problemSource);

    Submission submit(SubmissionInfoModel info , Authentication authentication);

    Page<ProblemsPageModel> searchByTitle(String title, Pageable pageable);

    Page<ProblemsPageModel> searchBySource(String source, Pageable pageable);

    Page<ProblemsPageModel> searchByProblemCode(String problemCode, Pageable pageable);
}
