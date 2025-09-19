package com.xjudge.service.problem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xjudge.entity.problem.Problem;
import com.xjudge.exception.ApiBaseException;
import com.xjudge.exception.NetworkScrapingException;
import com.xjudge.exception.NotImplementedException;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;

/**
 * Attempt to scrap problems asynchronously from supported online judges.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AsyncProblemScraper {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;
    private final Map<OnlineJudgeType, ScrappingStrategy> scrappingStrategies;

    @Async
    @Retryable(retryFor = { NetworkScrapingException.class }, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void scrapeAndSave(OnlineJudgeType ojType, String code) {
        log.info("ASYNC: Starting scraping process for problem {} from {}", code, ojType);

        ScrappingStrategy strategy = scrappingStrategies.get(ojType);

        if (strategy == null) {
            log.error("Unsupported origin: {}", ojType);
            throw new NotImplementedException("The platform " + ojType + " is not supported yet, will be added soon.");
        }

        Problem scrapedProblem = strategy.scrap(code);

        // Only update if it's already in the database
        problemRepository.findByCodeAndOnlineJudge(code, ojType)
                .ifPresent(existing -> scrapedProblem.setId(existing.getId()));

        problemRepository.save(scrapedProblem);
        /* TODO: Check why this fails later Then move to testing part
                Problem problemToSave = problemRepository.findByCodeAndOnlineJudge(code, ojType)
            .map(existingProblem -> {
                // Update a managed entity and save it to make use of dirty checking
                problemMapper.updateProblemFromSource(scrapedProblem, existingProblem);
                return existingProblem;
            })
            .orElse(scrapedProblem);

        problemRepository.save(problemToSave);

         */

        log.info("ASYNC: Successfully scraped and saved problem: {} - {}", code, scrapedProblem.getTitle());
    }

    @Recover
    public void recover(NetworkScrapingException ex, OnlineJudgeType ojType, String code) {
        log.error("ASYNC: Scraping failed due to network error after retries for problem {} from {}: {}", code, ojType, ex.getMessage());
        problemRepository.updateFetchingStatus(code, ojType, FetchingStatus.FAILED);
    }

    @Recover
    public void recover(ApiBaseException ex, OnlineJudgeType ojType, String code) {
        log.error("ASYNC: Scraping failed due to unsupported origin {} or wrong problem code {}: {}", ojType, code, ex.getMessage());
        problemRepository.updateFetchingStatus(code, ojType, FetchingStatus.FAILED);
    }
}
