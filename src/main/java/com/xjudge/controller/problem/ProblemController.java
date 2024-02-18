package com.xjudge.controller.problem;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.xjudge.entity.Problem;
import com.xjudge.service.problem.ProblemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
@Tag(name = "Problem", description = "The Problem End-Points for fetching & submitting problems.")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    @Operation(summary = "Retrieve all problems", description = "Get all problems available in the system with pagination.")
    public ResponseEntity<List<Problem>> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer size){
        Pageable paging = PageRequest.of(pageNo, size);
        Page<Problem> pagedResult = problemService.getAllProblems(paging);
        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{problemCode}")
    @Operation(summary = "Retrieve a specific problem", description = "Get a specific problem by its code.")
    public ResponseEntity<Problem> getProblem(@PathVariable String problemCode){
        return new ResponseEntity<>(problemService.getProblemByCode(problemCode), HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit a problem", description = "Submit a specific problem to be judged.")
    public ResponseEntity<Submission> submit(@Valid @RequestBody SubmissionInfoModel info){
        return new ResponseEntity<>(problemService.submit(info), HttpStatus.OK);
    }

}
