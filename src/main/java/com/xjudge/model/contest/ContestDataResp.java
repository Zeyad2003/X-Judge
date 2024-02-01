package com.xjudge.model.contest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xjudge.entity.*;
import com.xjudge.enums.ContestType;
import com.xjudge.enums.ContestVisibility;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.user.UserContestModel;
import com.xjudge.model.user.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

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
