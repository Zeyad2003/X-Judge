package com.xjudge.model.contest;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Used when returning the Contest data to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContestUserDataModel extends ContestBaseModel {

    @NotNull(message = "The contest id is required.")
    @Min(value = 1, message = "The contest id must be positive.")
    Long id;

    @NotNull(message = "The contest begin time is required.")
    @PastOrPresent(message = "The contest begin time must be in the past or present.")
    Instant beginTime;

    Boolean isFavourite;

    @NotNull(message = "The problems grid data can't be null.")
    @Size(min = 1, message = "At least one problem is required.")
    List<ProblemsData> problemsData;
}

record ProblemsData(

        @NotNull(message = "The problem code is required.")
        @NotBlank(message = "Problem code can't be empty.")
        String problemCode,

        @NotNull(message = "The problem status is required.")
        @NotBlank(message = "Problem status can't be empty.")
        String problemStatus, // Accepted, Attempted, Not Attempted

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
        String problemAlias
) {
}
