package com.xjudge.model.submission;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionInfoModel(

        @NotNull
        @NotBlank
        String contestId,

        @NotNull
        @NotBlank
        String problemId,

        @NotNull
        OnlineJudgeType ojType,

        Boolean isOpen,

        @NotNull
        @NotBlank
        String solutionCode,

        @NotNull
        Compiler compiler
) {}
