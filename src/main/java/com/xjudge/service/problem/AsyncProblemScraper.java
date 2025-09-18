package com.xjudge.service.problem;

import com.xjudge.entity.problem.Problem;
import com.xjudge.exception.ApiBaseException;
import com.xjudge.exception.BadRequestException;
import com.xjudge.exception.ScrapingException;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncProblemScraper {

    private final ProblemRepository problemRepository;
    private final Map<OnlineJudgeType, ScrappingStrategy> scrappingStrategies;

    @Async
    @Transactional
    @Retryable(retryFor = { ScrapingException.class, ApiBaseException.class }, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void scrapeAndSave(OnlineJudgeType ojType, String code) {
        log.info("ASYNC: Starting scraping process for problem {} from {}", code, ojType);

        ScrappingStrategy strategy = scrappingStrategies.get(ojType);

        if (strategy == null) {
            log.error("ASYNC: Unsupported origin: {}", ojType);
            throw new BadRequestException("Unsupported origin: " + ojType);
        }

        Problem scrapedProblem = strategy.scrap(code);
        scrapedProblem.setOnlineJudge(ojType);
        scrapedProblem.setCode(code);

        // If already exists, reuse its id -> save() will perform update
        problemRepository.findByCodeAndOnlineJudge(code, ojType)
                .ifPresent(existing -> scrapedProblem.setId(existing.getId()));

        Problem savedProblem = problemRepository.save(scrapedProblem);

        log.info("ASYNC: Successfully scraped and saved problem: {} - {}", code, savedProblem.getTitle());
    }

    @Recover
    @Transactional
    public void recover(ScrapingException ex, OnlineJudgeType ojType, String code) {
        log.error("ASYNC: Scraping failed due to network error after retries for problem {} from {}: {}", code, ojType, ex.getMessage());
        problemRepository.findByCodeAndOnlineJudge(code, ojType).ifPresent(problem -> {
            problem.setFetchingStatus(FetchingStatus.FAILED);
            problemRepository.save(problem);
        });
    }

    @Recover
    @Transactional
    public void recover(ApiBaseException exception, OnlineJudgeType ojType, String code) {
        log.error("ASYNC: Scraping failed due to API error after retries for problem {} from {}: {}", code, ojType, exception.getMessage());
        problemRepository.findByCodeAndOnlineJudge(code, ojType).ifPresent(problem -> {
            problem.setFetchingStatus(FetchingStatus.FAILED);
            problemRepository.save(problem);
        });
    }
}