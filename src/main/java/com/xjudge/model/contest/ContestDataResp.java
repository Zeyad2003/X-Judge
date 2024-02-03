package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.user.UserContestModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContestDataResp {

    private Long  contestId;

    private String contestTitle;

    private Instant contestBeginTime;

    private Duration contestLength;

    private String contestDescription;

    private ContestType contestType;

    private ContestVisibility contestVisibility;

    private List<UserContestModel> participants;

    private List<SubmissionModel> submissions;

}
