package com.xjudge.model.contest.modification;

import jakarta.validation.constraints.Min;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * The model that come when selecting the problemset for a contest.
 */
public record ContestProblemset(
        @NotNull(message = "The problem code is required.")
        @NotBlank(message = "The problem code is required.")
        String code,

        @NotNull(message = "The problem weight is required.")
        @Min(value = 1)
        Integer problemWeight,

        @NotNull(message = "You must select the online judge of problem.")
        OnlineJudgeType ojType,

        @NotNull(message = "The problem hashtag is required.")
        @NotBlank(message = "The problem hashtag is required.")
        String problemHashtag, // A, B, C, AA, AB, etc.

        String problemAlias
) {
}
