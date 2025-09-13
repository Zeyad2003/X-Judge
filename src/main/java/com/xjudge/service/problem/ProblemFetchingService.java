package com.xjudge.service.problem;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;

public interface ProblemFetchingService {

    ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code);
}
