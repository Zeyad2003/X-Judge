package com.xjudge.model.contest;

import jakarta.validation.constraints.Min;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record ContestProblemset(
        @NotNull
        @NotBlank
        String problemCode,

        @NotNull
        @Min(value = 1)
        Integer problemWeight,

        String problemAlias,

        @NotNull
        OnlineJudgeType ojType
) {}
