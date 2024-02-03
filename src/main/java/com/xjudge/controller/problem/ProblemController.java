package com.xjudge.controller.problem;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.xjudge.entity.Problem;
import com.xjudge.service.problem.ProblemService;
import com.xjudge.model.problem.ContestProblemResp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService service;

    @GetMapping("/findProblem/{problemCode}")
    public ResponseEntity<ContestProblemResp> getProblemContestData(@Valid @PathVariable @NotNull String problemCode){
        return new ResponseEntity<>(service.getProblemDataForContest(problemCode) , HttpStatus.OK);
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<Problem> getProblem(@PathVariable Long id){
        return new ResponseEntity<>(service.getProblem(id) , HttpStatus.OK);
    }
}
