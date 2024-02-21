package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * The base model that contain the common fields for the contest.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContestBaseModel {
    @NotNull(message = "The user handle is required to create a contest.")
    @NotBlank(message = "UserHandle can't be empty.")
    private String ownerHandle;

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

    private String groupName;

    private String password;

    private String description;

}
