package com.xjudge.model.contest;

import com.xjudge.model.enums.OnlineJudgeType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ContestStatusPageModel {
    private Long id;
    private String userHandle;
    private OnlineJudgeType ojType;
    private String problemCode;
    private String verdict;
    private String language;
    private String timeUsage;
    private String memoryUsage;
    private Instant submitTime;
    private String remoteRunId;
    private String problemHashtag;
}
