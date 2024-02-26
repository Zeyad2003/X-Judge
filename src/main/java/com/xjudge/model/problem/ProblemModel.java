package com.xjudge.model.problem;

import com.xjudge.model.enums.OnlineJudgeType;
import java.util.Map;

public record ProblemModel(
        Long id,

        String problemCode,

        String title,

        String statement,

        String input,

        String output,

        OnlineJudgeType source,

        String timeLimit,

        String memoryLimit,

        String problemHashtag,

        Map<String, Object> extraInfo

) {
}
