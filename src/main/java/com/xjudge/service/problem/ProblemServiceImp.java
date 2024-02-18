package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.SubmissionAutomation;
import com.xjudge.service.submission.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemServiceImp implements ProblemService {

    private final ProblemRepository problemRepo;
    private final GetProblemAutomation getProblemAutomation;
    private final SubmissionAutomation submissionAutomation;
    private final SubmissionService submissionService;

//    final String PROBLEM_NOT_FOUND = "PROBLEM_NOT_FOUND";
//
//    @Override
//    public ContestProblemResp getProblemDataForContest(String problemCode) {
//
//        int contestId = Integer.parseInt(problemCode.substring(0 , problemCode.length() - 1));
//        char problemId = problemCode.charAt(problemCode.length() - 1);
//        Problem problem = getProblemAutomation.GetProblem(contestId , problemId);
//
//        if(problem == null) throw new EntityNotFoundException(PROBLEM_NOT_FOUND);
//
//        return ContestProblemResp.builder()
//                .problemId(problem.getId())
//                .problemCode(problem.getProblemCode())
//                .title(problem.getProblemTitle())
//                .platform(problem.getProblemSource())
//                .build();
//    }

    @Override
    public Page<Problem> getAllProblems(Pageable pageable) {
        return problemRepo.findAll(pageable);
    }

    @Override
    public Problem getProblemById(Long problemId) {
        return problemRepo.findById(problemId).orElseThrow(() -> new XJudgeException("Problem not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public Problem getProblemByCode(String problemCode) {
        if (problemCode.startsWith("CodeForces")) {
            problemCode = problemCode.substring(11);
            Optional<Problem> problem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
            if (problem.isPresent()) return problem.get();
            return getProblem(problemCode);
        }
        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }

    @Override
    public Submission submit(SubmissionInfoModel info) {

        if (info.ojType() == OnlineJudgeType.CodeForces) {
            return submissionService.save(submissionAutomation.submit(info));
        }

        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }

    private Problem getProblem(String problemCode) {
        String contestId = problemCode.replaceAll("(\\d+).*", "$1");
        String problemId = problemCode.replaceAll("\\d+(.*)", "$1");

        Problem problem = getProblemAutomation.GetProblem(contestId, problemId);

        Optional<Problem> storedProblem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
        storedProblem.ifPresent(value -> problem.setId(value.getId()));

        problemRepo.save(problem);

        return problem;
    }
}
