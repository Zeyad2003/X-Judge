package com.xjudge.service.problem;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;

public interface ProblemFetchingInterface {
    ProblemDetails fetchByLink(String link);
    ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code);
}
