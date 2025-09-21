package com.xjudge.service.problem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.model.problem.ProblemPageModel;

public interface ProblemFetchingService {

    Page<ProblemPageModel> getAllProblems(Pageable pageable);

    Page<ProblemPageModel> getFilteredProblems(OnlineJudgeType ojType, String code, String title, String contestName,
            Pageable pageable);

    ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code);

    void triggerFetchOrUpdate(OnlineJudgeType ojType, String code);

    FetchingStatus getProblemFetchingStatus(OnlineJudgeType ojType, String code);
}
