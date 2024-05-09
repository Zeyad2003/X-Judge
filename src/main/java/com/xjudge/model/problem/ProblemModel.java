package com.xjudge.model.problem;

import com.xjudge.entity.Property;
import com.xjudge.model.enums.OnlineJudgeType;

import java.util.List;

public record ProblemModel(
        Long id,

        String code,

        String title,

        String problemLink,

        String contestName,

        String contestLink,

        OnlineJudgeType onlineJudge,

        String problemHashtag,

        List<Property> properties

) {
}
