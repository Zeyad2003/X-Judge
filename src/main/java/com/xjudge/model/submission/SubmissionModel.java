package com.xjudge.model.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionModel {

    private Long submissionId;

    private String submissionUserHandle;

    private String submissionProblemId;

    private String submissionCode;

    private String submissionLanguage;

    private Instant submissionTime;

    private String submissionVerdict;

    private BigDecimal submissionMemoryUsage;

    private BigDecimal submissionTimeUsage;
}
