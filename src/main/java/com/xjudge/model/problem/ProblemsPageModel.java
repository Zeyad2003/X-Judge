package com.xjudge.model.problem;

import com.xjudge.model.enums.OnlineJudgeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemsPageModel {
    OnlineJudgeType onlineJudge;

    String code;

    String contestId;

    String problemId;

    String title;

    String problemLink;

    String contestName;

    String contestLink;

    Integer solvedCount;
}
