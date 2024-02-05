package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.model.problem.ContestProblemResp;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProblemServiceImp implements ProblemService{

    ProblemRepository repository;
    GetProblemAutomation getProblemAutomation;
    final String PROBLEM_NOT_FOUND = "PROBLEM_NOT_FOUND";

    @Autowired
    public ProblemServiceImp(ProblemRepository repository , GetProblemAutomation getProblemAutomation) {
        this.repository = repository;
        this.getProblemAutomation =  getProblemAutomation;
    }


    @Override
    public ContestProblemResp getProblemDataForContest(String problemCode) {

        int contestId = Integer.parseInt(problemCode.substring(0 , problemCode.length() - 1));
        char problemId = problemCode.charAt(problemCode.length() - 1);
        Problem problem = getProblemAutomation.GetProblem(contestId , problemId);

        if(problem == null) throw new EntityNotFoundException(PROBLEM_NOT_FOUND);

        return ContestProblemResp.builder()
                .problemId(problem.getId())
                .problemCode(problem.getProblemCode())
                .title(problem.getProblemTitle())
                .platform(problem.getProblemSource())
                .build();
    }

    @Override
    public Problem getProblem(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(PROBLEM_NOT_FOUND));
    }
}
