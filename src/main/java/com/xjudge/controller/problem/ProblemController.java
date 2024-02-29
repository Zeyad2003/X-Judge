package com.xjudge.controller.problem;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.problem.ProblemsPageModel;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
@Tag(name = "Problem", description = "The Problem End-Points for fetching & submitting problems.")
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemMapper problemMapper;

    @GetMapping
    @Operation(summary = "Retrieve all problems", description = "Get all problems available in the system with pagination.")
    public ResponseEntity<List<ProblemsPageModel>> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                  @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ProblemsPageModel> pagedResult = problemService.getAllProblems(paging);
        return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{problemSource}-{problemCode}")
    @Operation(summary = "Retrieve a specific problem", description = "Get a specific problem by its code.")
    public ResponseEntity<ProblemModel> getProblem(@PathVariable("problemCode") String problemCode , @PathVariable("problemSource") String problemSource){
        Problem problem = problemService.getProblemByCodeAndSource(problemCode, problemSource);
        return new ResponseEntity<>(problemMapper.toModel(problem), HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit a problem", description = "Submit a specific problem to be judged.")
    public ResponseEntity<Submission> submit(@Valid @RequestBody SubmissionInfoModel info , BindingResult result){
        if(result.hasErrors()) throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR ,ProblemController.class.getName(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(problemService.submit(info), HttpStatus.OK);
    }

}
