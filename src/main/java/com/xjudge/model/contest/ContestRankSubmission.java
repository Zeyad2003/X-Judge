package com.xjudge.model.contest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContestRankSubmission {

    private Integer submissionId;

    private String problemIndex;

    private Long submitTime;

    private String status;

    private String memory;

    private String time;

    private String language;

}