package com.xjudge.model.submission;

import com.xjudge.model.enums.OnlineJudgeType;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionModel {

    private Long id;

    private String remoteRunId;

    private OnlineJudgeType ojType;

    private String language;

    private Instant submitTime;

    private String memoryUsage;

    private String timeUsage;

    private String verdict;

    private Boolean isOpen;

    private String submissionStatus;

    private String problemCode;

    private String userHandle;

    private Long contestId;

}
