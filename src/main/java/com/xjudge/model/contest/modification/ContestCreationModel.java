package com.xjudge.model.contest.modification;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

/**
 * The model that should be sent when creating a contest.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContestCreationModel extends ContestModificationModel {
    @NotNull(message = "The contest begin time is required to create a contest.")
    @Future(message = "The contest begin time must be in the future.")
    private Instant beginTime;
}
