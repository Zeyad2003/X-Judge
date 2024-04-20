package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestStatus;
import com.xjudge.model.enums.ContestVisibility;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
public class ContestModel {
    private Long id;

    private String title;

    private Instant beginTime;

    private Instant endTime;

    private Duration duration;

    private ContestVisibility visibility;

    private ContestStatus contestStatus;

    private String ownerHandle;

    private String ownerId;

    private String groupId;

    private String groupName;

    private List<ContestProblemModel> problemSet;

}
