package com.xjudge.model.contest;

import jakarta.validation.constraints.Min;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * The model that come when selecting the problemset for a contest.
 */
public record ContestProblemset(
        @NotNull
        @NotBlank
        String problemCode,

        @NotNull
        @Min(value = 1)
        Integer problemWeight,

        @NotNull
        OnlineJudgeType ojType,

        @NotNull
        @NotBlank
        String problemHashtag, // A, B, C, AA, AB, etc.

        String problemAlias
) {
}
