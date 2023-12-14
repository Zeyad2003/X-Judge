package com.xjudge.controllers.codeforces;

import com.xjudge.model.SubmissionInfo;
import com.xjudge.model.SubmissionResult;
import com.xjudge.service.problemscraping.SubmissionAutomation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/submit")
public class SubmissionController {

    private final SubmissionAutomation service;

    @GetMapping
    public String welcomeMessage() {
        return "HELLO, THE SERVER IS WORKING :)";
    }

    @PostMapping("/{problemCode}")
    ResponseEntity<SubmissionResult> submit(@PathVariable String problemCode, @RequestBody @Valid SubmissionInfo data) {
        return new ResponseEntity<>(service.submit(problemCode, data), HttpStatus.OK);
    }

}
