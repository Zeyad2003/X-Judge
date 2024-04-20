package com.xjudge.model.contest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContestRankModel {

    private Long userId;

    private String handle;

    private String photoUrl;

    private String firstName;

    private String lastName;

    private Integer numOfAccepted;

    private Long userContestPenalty;

    private Integer userContestScore;

    private List<ContestRankSubmission> submissionList;
}
