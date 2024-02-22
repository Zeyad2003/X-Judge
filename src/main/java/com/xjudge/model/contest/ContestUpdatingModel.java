package com.xjudge.model.contest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * The model that should be sent when updating a contest.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContestUpdatingModel extends ContestBaseModel {

    @NotNull(message = "The problemset can't be null.")
    @Size(min = 1, message = "At least one problem is required to create a contest.")
    private List<ContestProblemset> problems;
}
