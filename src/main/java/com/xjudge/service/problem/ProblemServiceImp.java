package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.SubmissionAutomation;
import com.xjudge.service.submission.SubmissionService;
import com.xjudge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemServiceImp implements ProblemService {

    private final ProblemRepository problemRepo;
    private final GetProblemAutomation getProblemAutomation;
    private final SubmissionAutomation submissionAutomation;
    private final SubmissionService submissionService;
    private final UserService userService;

    @Override
    public Page<Problem> getAllProblems(Pageable pageable) {
        return problemRepo.findAll(pageable);
    }

    @Override
    public Problem getProblemByCodeAndSource(String problemCode, String problemSource) {
        if (problemSource.equalsIgnoreCase("codeforces")) {
            Optional<Problem> problem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
            return problem.orElseGet(() -> scrapProblem(problemCode));
        }
        throw new XJudgeException("Online Judge not supported yet.", ProblemServiceImp.class.getName(), HttpStatus.NOT_FOUND);
    }

    @Override
    public Submission submit(SubmissionInfoModel info) {
        User user = userService.getUserByHandle(info.userHandle());

        if (info.ojType() == OnlineJudgeType.CodeForces) {
            Problem problem = getProblemByCodeAndSource(info.problemCode(), info.ojType().name());
            Submission submission = submissionAutomation.submit(info);

            submission.setProblem(problem);
            submission.setUser(user);

            return submissionService.save(submission);
        }

        throw new XJudgeException("Online Judge not supported yet.", ProblemServiceImp.class.getName(), HttpStatus.NOT_FOUND);
    }

    private Problem scrapProblem(String problemCode) {
        String contestId = problemCode.replaceAll("(\\d+).*", "$1");
        String problemId = problemCode.replaceAll("\\d+(.*)", "$1");

        Problem problem = getProblemAutomation.GetProblem(contestId, problemId);

        Optional<Problem> storedProblem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
        storedProblem.ifPresent(value -> problem.setId(value.getId()));

        problemRepo.save(problem);

        return problem;
    }
}
