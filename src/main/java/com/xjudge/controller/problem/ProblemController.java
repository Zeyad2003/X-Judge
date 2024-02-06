package com.xjudge.controller.problem;

import lombok.RequiredArgsConstructor;

import com.xjudge.entity.Problem;
import com.xjudge.service.problem.ProblemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService service;

    @GetMapping("/problem/{problemCode}")
    public ResponseEntity<Problem> getProblem(@PathVariable String problemCode){
        return new ResponseEntity<>(service.getProblem(problemCode), HttpStatus.OK);
    }
}
