package com.xjudge.model.contest.modification;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * The base model that contain the common fields for the contest.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestClientRequest {

    @NotBlank(message = "The contest title is required.")
    @NotNull(message = "The contest title is required.")
    private String title;

    @NotNull(message = "The contest length is required.")
    @Max(value = 31536000, message = "The contest length must be less than 1 year.")
    private Integer durationSeconds;

    @NotNull(message = "Contest type can't be empty.")
    private ContestType type;

    @NotNull(message = "The contest visibility is required.")
    private ContestVisibility visibility;

    private Long groupId = 0L;

    private String password;

    private String description;

    @NotNull(message = "The contest begin time is required.")
    private Instant beginTime;

    @NotNull(message = "The problem Set required.")
    @Size(min = 1, message = "At least one problem is required to create a contest.")
    private List<ContestProblemset> problems;

}
