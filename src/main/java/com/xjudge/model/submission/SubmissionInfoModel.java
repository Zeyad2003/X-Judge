package com.xjudge.model.submission;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionInfoModel(

        @NotNull(message = "The problem code is required to submit a solution.")
        @NotBlank(message = "The problem code can't be empty.")
        String code,

        @NotNull(message = "The online judge is required to submit a solution.")
        OnlineJudgeType ojType,

        Boolean isOpen,

        @NotNull(message = "The solution code is required to submit a solution.")
        @NotBlank(message = "The solution code can't be empty.")
        String solutionCode,

        @NotNull(message = "The compiler is required to submit a solution.")
        Compiler compiler
) {}
