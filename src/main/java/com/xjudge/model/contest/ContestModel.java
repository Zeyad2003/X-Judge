package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContestModel {

    private Long contestId;

    private String contestTitle;

    private Instant contestBeginTime;

    private Duration contestLength;

    private String contestDescription;

    private ContestType contestType;

    private ContestVisibility contestVisibility;
}
