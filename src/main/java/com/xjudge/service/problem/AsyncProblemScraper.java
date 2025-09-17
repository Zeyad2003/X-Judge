package com.xjudge.service.problem;

import com.xjudge.entity.problem.Problem;
import com.xjudge.exception.BadRequestException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
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
    @Retryable(backoff = @Backoff(delay = 2000, multiplier = 2))
    public void scrapeAndSave(OnlineJudgeType ojType, String code) {
        log.info("ASYNC: Starting scraping process for problem {} from {}", code, ojType);

        ScrappingStrategy strategy = scrappingStrategies.get(ojType);

        if (strategy == null) {
            log.error("ASYNC: Unsupported origin: {}", ojType);
            throw new BadRequestException("Unsupported origin: " + ojType);
        }

        try {
            Problem scrapedProblem = strategy.scrap(code);
            scrapedProblem.setOnlineJudge(ojType);
            scrapedProblem.setCode(code);

            // If already exists, reuse its id → save() will perform update
            problemRepository.findByCodeAndOnlineJudge(code, ojType)
                    .ifPresent(existing -> scrapedProblem.setId(existing.getId()));

            Problem savedProblem = problemRepository.save(scrapedProblem);

            log.info(
                    "ASYNC: Successfully scraped and saved problem: {} - {}",
                    code,
                    savedProblem.getTitle());
        } catch (Exception e) {
            log.error("ASYNC: Failed to scrape and save problem {} from {}", code, ojType, e);
        }
    }
}