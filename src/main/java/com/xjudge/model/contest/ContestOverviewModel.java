package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestVisibility;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.*;

public record ContestOverviewModel(
        @NotNull(message = "The problems grid data can't be null.")
        @Size(min = 1, message = "At least one problem is required.")
        List<ProblemsGridData> problemsGridData,

        @NotNull(message = "The contest title is required.")
        @NotBlank(message = "Contest title can't be empty.")
        String contestTitle,

        @NotNull(message = "The contest begin time is required.")
        @PastOrPresent(message = "The contest begin time must be in the past or present.")
        Instant contestBeginTime,

        @NotNull(message = "The contest duration is required.")
        Duration contestDuration,

        @NotNull(message = "The contest visibility is required.")
        ContestVisibility contestVisibility,

        @NotNull(message = "The contest owner handle is required.")
        @NotBlank(message = "Contest owner handle can't be empty.")
        String contestOwnerHandle,

        @NotNull(message = "The contest status is required.")
        @NotBlank(message = "Contest status can't be empty.")
        String contestStatus,

        String groupName,

        Boolean isFavourite
) {
}

record ProblemsGridData(
        @NotNull(message = "The problem status is required.")
        @NotBlank(message = "Problem status can't be empty.")
        String problemStatus,

        @NotNull(message = "The number of accepted is required.")
        @Min(value = 0, message = "The number of accepted can't be negative.")
        Integer numberOfAccepted,

        @NotNull(message = "The number of attempted is required.")
        @Min(value = 0, message = "The number of attempted can't be negative.")
        Integer numberOfAttempted,

        @NotNull(message = "The problem origin is required.")
        @NotBlank(message = "Problem origin can't be empty.")
        String problemOrigin,

        @NotNull(message = "The problem title is required.")
        @NotBlank(message = "Problem title can't be empty.")
        String problemTitle
) {
}