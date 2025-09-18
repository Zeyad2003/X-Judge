package com.xjudge.service.problem;

import com.xjudge.entity.problem.Problem;
import com.xjudge.exception.NotFoundException;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProblemFetchingServiceImpl implements ProblemFetchingService {

    private final ProblemMapper problemMapper;
    private final AsyncProblemScraper asyncProblemScraper;
    private final ProblemRepository problemRepository;

    @Override
    @Transactional(readOnly = true)

    public ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code) {
        log.info("Looking for problem: {} from {} within database", code, ojType);

        return problemRepository.findByCodeAndOnlineJudge(code, ojType)
                .map(problemMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Problem not found in the database. Please fetch it first."));
    }

    @Override
    public void triggerFetchOrUpdate(OnlineJudgeType ojType, String code) {
        log.info("Triggering async fetch/update for problem: {} from {}", code, ojType);
        Problem problem = Problem.builder()
                .code(code)
                .onlineJudge(ojType)
                .fetchingStatus(FetchingStatus.IN_PROGRESS)
                .build();
        problemRepository.save(problem);

        asyncProblemScraper.scrapeAndSave(ojType, code);
        log.info("Async fetch/update for problem {} has been dispatched.", code);
    }

    @Override
    public FetchingStatus getProblemFetchingStatus(OnlineJudgeType ojType, String code) {
        return problemRepository.findByCodeAndOnlineJudge(code, ojType)
                .map(Problem::getFetchingStatus)
                .orElse(FetchingStatus.NOT_STARTED);
    }
}

