package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.time.DurationMax;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record ContestCreationModel(
        @NotNull(message = "The user handle is required to create a contest.")
        @NotBlank(message = "UserHandle can't be empty.")
        String userHandle,

        @NotNull(message = "The contest title is required to create a contest.")
        @NotBlank(message = "Contest title can't be empty.")
        String contestTitle,

        @NotNull(message = "The contest begin time is required to create a contest.")
        @Future(message = "The contest begin time must be in the future.")
        Instant contestBeginTime,

        @NotNull(message = "The contest length is required to create a contest.")
        @DurationMax(days = 365, message = "The contest length must be less than 1 year.")
        Duration contestLength,

        String contestDescription,

        @NotNull(message = "You must select the type of the contest.")
        ContestType contestType,

        String groupName,

        @NotNull(message = "You must select the visibility of the contest.")
        ContestVisibility contestVisibility,

        @NotNull(message = "The problemset can't be null.")
        @Size(min = 1, message = "At least one problem is required to create a contest.")
        List<ContestProblemset> problems
) {
}