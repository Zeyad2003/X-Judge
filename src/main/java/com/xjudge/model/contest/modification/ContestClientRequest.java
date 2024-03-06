package com.xjudge.model.contest.modification;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @NotNull(message = "The contest title is required to create a contest.")
    @NotBlank(message = "Contest title can't be empty.")
    private String title;

    @NotNull(message = "The contest length is required to create a contest.")
    @Max(value = 31536000, message = "The contest length must be less than 1 year.")
    private Integer durationSeconds;

    @NotNull(message = "You must select the type of the contest.")
    private ContestType type;

    @NotNull(message = "You must select the visibility of the contest.")
    private ContestVisibility visibility;

    @Min(value = 1 , message = "groupId can not be less than 1")
    private Long groupId = 0L;

    private String password;

    private String description;

    @NotNull(message = "The contest begin time is required to create a contest.")
    @Future(message = "The contest begin time must be in the future.")
    private Instant beginTime;

    @NotNull(message = "The problem Set can't be null.")
    @Size(min = 1, message = "At least one problem is required to create a contest.")
    private List<ContestProblemset> problems;

}
