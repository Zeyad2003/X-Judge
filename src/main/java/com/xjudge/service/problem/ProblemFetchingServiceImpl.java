package com.xjudge.service.problem;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjudge.entity.problem.Problem;
import com.xjudge.exception.NotFoundException;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.model.problem.ProblemPageModel;
import com.xjudge.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProblemFetchingServiceImpl implements ProblemFetchingService {

    private final ProblemMapper problemMapper;
    private final AsyncProblemScraper asyncProblemScraper;
    private final ProblemRepository problemRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProblemPageModel> getAllProblems(Pageable pageable) {
        Page<ProblemPageModel> problemsPage = problemRepository.findAllForPageModel(pageable);
        return problemsPage;
    }

    @Override
    public Page<ProblemPageModel> getFilteredProblems(OnlineJudgeType ojType, String code, String title,
            String contestName, Pageable pageable) {
        return problemRepository.findProblemsByFiltration(ojType, code, title, contestName, pageable);
    }

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

        Boolean exists = problemRepository.existsByCodeAndOnlineJudge(code, ojType);

        if (!exists) {
            Problem problem = Problem.builder()
                    .code(code)
                    .onlineJudge(ojType)
                    .fetchingStatus(FetchingStatus.IN_PROGRESS)
                    .build();

            problemRepository.save(problem);
        } else {
            FetchingStatus status = problemRepository.getFetchingStatus(code, ojType).get();
            Instant lastModifiedDate = problemRepository.getLastModifiedDate(code, ojType).get();

            if (status == FetchingStatus.IN_PROGRESS ||
                    (status == FetchingStatus.SUCCESSFUL && lastModifiedDate.isAfter(Instant.now().minusSeconds(10)))) {

                log.info("Problem {} was fetched recently. Skipping fetch/update.", code);
                return;
            }
        }

        asyncProblemScraper.scrapeAndSave(ojType, code);
        log.info("Fetch/update for problem {} has been dispatched.", code);
    }

    @Override
    public FetchingStatus getProblemFetchingStatus(OnlineJudgeType ojType, String code) {
        return problemRepository.getFetchingStatus(code, ojType)
                .orElse(FetchingStatus.NOT_STARTED);
    }
}
