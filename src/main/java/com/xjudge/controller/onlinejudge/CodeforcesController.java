package com.xjudge.controller.onlinejudge;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.SubmissionAutomation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codeforces")
public class CodeforcesController {

    private final GetProblemAutomation problem;
    private final SubmissionAutomation service;

    @GetMapping
    public String welcomeMessage() {
        return "HELLO, THE SERVER IS UP & RUNNING :)";
    }

    @GetMapping("/{contestId}/{problemId}")
    public ResponseEntity<?> getProblem(@PathVariable Integer contestId, @PathVariable Character problemId) {
        return ResponseEntity.ok(problem.GetProblem(contestId, problemId));
    }

    @PostMapping("/submit/{problemCode}")
    ResponseEntity<SubmissionResult> submit(@PathVariable String problemCode, @RequestBody @Valid SubmissionInfo data) {
        return new ResponseEntity<>(service.submit(problemCode, data), HttpStatus.OK);
    }
}
