package com.xjudge.controller.problem;

import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;
import lombok.RequiredArgsConstructor;

import com.xjudge.entity.Problem;
import com.xjudge.service.problem.ProblemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/{problemCode}")
    public ResponseEntity<Problem> getProblem(@PathVariable String problemCode){
        return new ResponseEntity<>(problemService.getProblem(problemCode), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmissionResult> submit(@RequestBody SubmissionInfo info){
        return new ResponseEntity<>(problemService.submit(info), HttpStatus.OK);
    }
}
