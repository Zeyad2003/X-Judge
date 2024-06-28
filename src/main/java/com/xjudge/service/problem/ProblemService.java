package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.model.problem.ProblemDescription;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.problem.ProblemsPageModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.user.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.security.Principal;


public interface ProblemService {
    Page<ProblemsPageModel> getAllProblems(Pageable pageable);

    Page<ProblemsPageModel> filterProblems(String source, String problemCode, String title, String contestName, Pageable pageable);

    Problem getProblem(String source, String code);

    ProblemDescription getProblemDescription(String source, String code);

    ProblemModel getProblemModel(String source, String code);

    Submission submit(SubmissionInfoModel info , Authentication authentication);

    SubmissionModel submitClient(SubmissionInfoModel info , Authentication authentication);

    Page<ProblemsPageModel> searchByTitle(String title, Pageable pageable);

    Page<ProblemsPageModel> searchBySource(String source, Pageable pageable);

    Page<ProblemsPageModel> searchByProblemCode(String problemCode, Pageable pageable);

    Statistics getStatistics(Principal connectedUser);

}
