package com.xjudge.model.submission;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SubmissionInfoModel(

        @NotBlank(message = "The problem code is required to submit a solution.")
        @NotNull(message = "The problem code can't be empty.")
        String code,

        @NotNull(message = "The online judge can't be empty.")
        OnlineJudgeType ojType,

        Boolean isOpen,

        @NotBlank(message = "The solution code is required to submit a solution.")
        @NotNull(message = "The solution code can't be empty.")
        String solutionCode,

        @NotNull(message = "The compiler can't be empty.")
        Compiler compiler
) {}
