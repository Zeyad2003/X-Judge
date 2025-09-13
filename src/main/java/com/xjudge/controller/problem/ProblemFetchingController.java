package com.xjudge.controller.problem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.service.problem.ProblemFetchingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("problem")
public class ProblemFetchingController {
    private final ProblemFetchingService problemFetchingService;

    @GetMapping("/{origin}/{code}")
    public ProblemDetails fetchByOriginAndCode(@PathVariable("origin") OnlineJudgeType origin,
            @PathVariable("code") String code) {
        return problemFetchingService.fetchByOriginAndCode(origin, code);
    }
}
