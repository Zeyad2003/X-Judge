package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestCreationRequest {

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private Long userId;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private String contestTitle;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private Instant contestBeginTime;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private Duration contestLength;

    private String contestDescription;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private Long groupId;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private ContestType contestType;

    @NotNull(message = "THIS FIELD CAN NOT BE EMPTY")
    private ContestVisibility contestVisibility;


    @NotNull(message = "SHOULD BE AT LEAST ONE PROBLEM")
    private List<ContestProblemData> problems;
}
