package com.xjudge.model.submission;

import com.xjudge.model.enums.OnlineJudgeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionPageModel {
    private Long id;
    private String userHandle;
    private OnlineJudgeType ojType;
    private String problemCode;
    private String verdict;
    private String language;
    private String timeUsage;
    private String memoryUsage;
    private Instant submitTime;
    private Boolean isOpen;
    private String remoteRunId;
}
