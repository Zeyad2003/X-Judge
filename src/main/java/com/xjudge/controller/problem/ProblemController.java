package com.xjudge.controller.problem;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.Pagination.PaginationResponse;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
@Tag(name = "Problem", description = "The Problem End-Points for fetching & submitting problems.")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    @Operation(summary = "Retrieve all problems", description = "Get all problems available in the system with pagination.")
    public ResponseEntity<?> getAllProblems(@RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<Problem> pagedResult = problemService.getAllProblems(paging);
        PaginationResponse<Problem> paginatedData = new PaginationResponse<>(pagedResult.getTotalPages(), pagedResult.getContent());
        Response<PaginationResponse<Problem>> response = new Response<>(HttpStatus.OK.value(), true, paginatedData, "Problems fetched successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{problemSource}-{problemCode}")
    @Operation(summary = "Retrieve a specific problem", description = "Get a specific problem by its code.")
    public ResponseEntity<Problem> getProblem(@PathVariable("problemCode") String problemCode , @PathVariable("problemSource") String problemSource){
        return new ResponseEntity<>(problemService.getProblemByCodeAndSource(problemCode, problemSource), HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit a problem", description = "Submit a specific problem to be judged.")
    public ResponseEntity<Submission> submit(@Valid @RequestBody SubmissionInfoModel info , BindingResult result){
        if(result.hasErrors()) throw new XJudgeValidationException(result.getFieldErrors() ,XJudgeValidationException.VALIDATION_ERROR ,ProblemController.class.getName(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(problemService.submit(info), HttpStatus.OK);
    }

}
