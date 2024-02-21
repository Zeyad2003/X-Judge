package com.xjudge.model.contest;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * The model that should be sent when creating a contest.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContestCreationModel extends ContestBaseModel {
    @NotNull(message = "The contest begin time is required to create a contest.")
    @Future(message = "The contest begin time must be in the future.")
    private Instant beginTime;

    @NotNull(message = "The problemset can't be null.")
    @Size(min = 1, message = "At least one problem is required to create a contest.")
    private List<ContestProblemset> problems;
}
