package com.xjudge.controller.problem;

import lombok.RequiredArgsConstructor;

import com.xjudge.entity.Problem;
import com.xjudge.service.problem.ProblemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemCode}")
    public ResponseEntity<Problem> getProblem(@PathVariable String problemCode){
        return new ResponseEntity<>(problemService.getProblem(problemCode), HttpStatus.OK);
    }
}
