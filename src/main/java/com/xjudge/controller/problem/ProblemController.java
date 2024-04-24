package com.xjudge.controller.problem;

import com.xjudge.exception.XJudgeValidationException;

import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.problem.ProblemsPageModel;
import com.xjudge.model.response.Response;

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
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
@Tag(name = "Problem", description = "The Problem End-Points for fetching & submitting problems.")
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemMapper problemMapper;

    @GetMapping
    @Operation(summary = "Retrieve all problems", description = "Get all problems available in the system with pagination.")
    public ResponseEntity<?> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ProblemsPageModel> paginatedData = problemService.getAllProblems(paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{problemSource}-{problemCode}")
    @Operation(summary = "Retrieve a specific problem", description = "Get a specific problem by its code.")
    public ResponseEntity<?> getProblem(@PathVariable("problemCode") String problemCode , @PathVariable("problemSource") String problemSource) {
        Problem problem = problemService.getProblemByCodeAndSource(problemCode, problemSource);
        Response response = Response.builder()
                .success(true)
                .data(problemMapper.toModel(problem))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit a problem", description = "Submit a specific problem to be judged.")
    public ResponseEntity<?> submit(@Valid @RequestBody SubmissionInfoModel info , BindingResult result , Authentication authentication){
        if(result.hasErrors()) throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR ,ProblemController.class.getName(),HttpStatus.BAD_REQUEST);
        Response response = Response.builder()
                .success(true)
                .data(problemService.submit(info , authentication))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search problems by title", description = "Search problems by their title.")
    public ResponseEntity<?> searchByTitle(@RequestParam String title, @RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ProblemsPageModel> paginatedData = problemService.searchByTitle(title, paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/source")
    @Operation(summary = "Search problems by source", description = "Search problems by their source.")
    public ResponseEntity<?> searchBySource(@RequestParam String source, @RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ProblemsPageModel> paginatedData = problemService.searchBySource(source, paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code")
    @Operation(summary = "Search problems by problem code", description = "Search problems by their problem code.")
    public ResponseEntity<?> searchByProblemCode(@RequestParam String problemCode, @RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<ProblemsPageModel> paginatedData = problemService.searchByProblemCode(problemCode, paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
