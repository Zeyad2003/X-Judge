package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mappers.ProblemMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.SubmissionAutomation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemServiceImp implements ProblemService {

    private final ProblemRepository problemRepo;
    private final ProblemMapper problemMapper;
    private final GetProblemAutomation getProblemAutomation;
    private final SubmissionAutomation submissionAutomation;
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
    public ProblemModel getProblem(String problemCode) {

        if (problemCode.startsWith("CodeForces")) {
            problemCode = problemCode.substring(11);
            Optional<Problem> problem = problemRepo.findByProblemCodeAndProblemSource(problemCode, OnlineJudgeType.CODEFORCES);
            if (problem.isPresent()) return problemMapper.toModel(problem.get());

            String contestId = problemCode.replaceAll("(\\d+).*", "$1");
            String problemId = problemCode.replaceAll("\\d+(.*)", "$1");

            Problem newProblem = getProblemAutomation.GetProblem(contestId, problemId);

            problemRepo.save(newProblem);

            return problemMapper.toModel(newProblem);
        }

        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }

    @Override
    public SubmissionResult submit(SubmissionInfo info) {

        if (info.onlineJudgeType() == OnlineJudgeType.CODEFORCES) {
            return submissionAutomation.submit(info);
        }

        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }
}
