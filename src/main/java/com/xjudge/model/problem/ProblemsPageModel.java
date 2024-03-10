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
    OnlineJudgeType oj;

    String problemCode;

    String problemTitle;

    String problemLink;

    String contestLink;

    Integer solvedCount;
}
