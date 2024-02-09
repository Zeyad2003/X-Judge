package com.xjudge.model.submission;

import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SubmissionInfo(
        @NotNull
        @NotBlank
        String problemCode,

        @NotNull
        OnlineJudgeType onlineJudgeType,

        @NotNull
        @NotBlank
        String solutionCode,

        @NotNull
        @PositiveOrZero
        Integer compilerId
) {}
