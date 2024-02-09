package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mappers.ProblemMapper;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.SubmissionAutomation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemServiceImp implements ProblemService{

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
        Optional<Problem> problem = problemRepo.findByProblemCode(problemCode);
        if(problem.isPresent()) return problemMapper.toModel(problem.get());

        if(problemCode.startsWith("CodeForces")) {
            Pair<String, String> codeForcesData = getCodeForcesCodeHelper(problemCode);
            String contestId = codeForcesData.getFirst();
            String problemId = codeForcesData.getSecond();

            Problem newProblem = getProblemAutomation.GetProblem(contestId, problemId);

            problemRepo.save(newProblem);

            return problemMapper.toModel(newProblem);
        }

        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }

    @Override
    public SubmissionResult submit(SubmissionInfo info) {
        String problemCode = info.getProblemCode();
        if(problemCode.startsWith("CodeForces")) {
            Pair<String, String> codeForcesCode = getCodeForcesCodeHelper(problemCode);
            problemCode = codeForcesCode.getFirst() + codeForcesCode.getSecond();
            return submissionAutomation.submit(problemCode, info);
        }

        throw new XJudgeException("Online Judge not supported yet.", HttpStatus.NOT_FOUND);
    }

    private Pair<String, String> getCodeForcesCodeHelper(String problemCode) {
        String contestId = problemCode.replaceAll("CodeForces-(\\d+).*", "$1");
        String problemId = problemCode.replaceAll("CodeForces-\\d+(.*)", "$1");
        return Pair.of(contestId, problemId);
    }
}
