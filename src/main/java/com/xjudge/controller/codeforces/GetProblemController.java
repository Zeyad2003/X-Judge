package com.xjudge.controller.codeforces;

import com.xjudge.service.problemscraping.GetProblemAutomation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/problem")
public class GetProblemController {

    private final GetProblemAutomation problem;

    @GetMapping("/{contestId}/{problemId}")
    public ResponseEntity<?> getProblem(@PathVariable int contestId, @PathVariable char problemId) {
        return ResponseEntity.ok(problem.GetProblem(contestId, problemId));
    }
}
