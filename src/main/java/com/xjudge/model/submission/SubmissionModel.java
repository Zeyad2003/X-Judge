package com.xjudge.model.submission;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Problem;
import com.xjudge.entity.User;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionModel {

    private Long id;

    private String remoteRunId;

    private OnlineJudgeType ojType;

    @Column(columnDefinition = "LONGTEXT")
    @Size(min = 20, max = 65535)
    private String solution;

    private String language;

    private Instant submitTime;

    private String memoryUsage;

    private String timeUsage;

    private String verdict;

    private Boolean isOpen;

    private String submissionStatus; // kept updated (submitted, in queue, running test 14, Accepted)

    private String problemCode;

    private String userHandle;

    private Long contestId;

}
