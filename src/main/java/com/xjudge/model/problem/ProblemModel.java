package com.xjudge.model.problem;

import com.xjudge.entity.Sample;
import com.xjudge.model.enums.OnlineJudgeType;

import java.util.List;
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

        List<Sample> samples,

        Map<String, Object> extraInfo

) {
}
