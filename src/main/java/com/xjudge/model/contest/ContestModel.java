package com.xjudge.model.contest;

import java.time.Instant;
import java.util.List;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used when returning the Contest data to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestModel {
    @NotNull(message = "The contest id is required.")
    @Min(value = 1, message = "The contest id must be positive.")
    Long id;

    @NotNull(message = "The user handle is required.")
    @NotBlank(message = "UserHandle can't be empty.")
    String userHandle;

    @NotNull(message = "The contest title is required.")
    @NotBlank(message = "Contest title can't be empty.")
    String title;

    @NotNull(message = "The contest length is required.")
    @Max(value = 31536000, message = "The contest length must be less than 1 year.")
    Integer durationSeconds;

    @NotNull(message = "The contest begin time is required.")
    private Instant beginTime;

    @NotNull(message = "You must select the type of the contest.")
    ContestType type;

    @NotNull(message = "You must select the visibility of the contest.")
    ContestVisibility visibility;

    String groupName;

    String password;

    String description;

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
        String problemAlias,

        @NotNull(message = "The problem hashtag is required.")
        @NotBlank(message = "Problem hashtag can't be empty.")
        String problemHashtag

) {
}
