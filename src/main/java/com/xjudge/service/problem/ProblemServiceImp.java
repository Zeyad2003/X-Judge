package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ContestProblemResp;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemServiceImp implements ProblemService{

    private final ProblemRepository problemRepo;
    private final GetProblemAutomation getProblemAutomation;
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
    public Problem getProblem(String problemCode) {
        Optional<Problem> problem = problemRepo.findByProblemCode(problemCode);
        if(problem.isPresent()) return problem.get();

        int contestId = Integer.parseInt(problemCode.substring(11, problemCode.length() - 1));
        char problemId = problemCode.charAt(problemCode.length() - 1);

        Problem newProblem = getProblemAutomation.GetProblem(contestId, problemId);

        problemRepo.save(newProblem);

        return newProblem;
    }
}
