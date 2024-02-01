package com.xjudge.model.contest;

import com.xjudge.entity.ContestProblem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.User;
import com.xjudge.enums.ContestType;
import com.xjudge.enums.ContestVisibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestCreationRequest {
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
