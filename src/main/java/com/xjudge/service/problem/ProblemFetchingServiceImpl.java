package com.xjudge.service.problem;

import com.xjudge.exception.NotFoundException;
import com.xjudge.mapper.ProblemMapper;
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
        // Just delegate to the async worker. The worker can decide whether to
        // create a new entity or update an existing one if you add that logic later.
        // For now, it always fetches and saves, which effectively updates the content.
        asyncProblemScraper.scrapeAndSave(ojType, code);
        log.info("Async fetch/update for problem {} has been dispatched.", code);
    }

    /** Scrapes a new problem from the online judge and saves it to our database */
   /* private ProblemDetails scrapeAndSaveProblem(OnlineJudgeType ojType, String code) {
        log.info("Problem {} not found in database, scraping from {}", code, ojType);

        ScrappingStrategy strategy = scrappingStrategies.get(ojType);
        if (strategy == null) {
            throw new BadRequestException("Unsupported origin: " + ojType);
        }

        Problem scrapedProblem = strategy.scrap(code);

        Problem savedProblem = problemRepository.save(scrapedProblem);

        log.info(
                "Successfully scraped and saved problem: {} - {} with {} sections, {} properties, {} samples",
                code,
                savedProblem.getTitle(),
                savedProblem.getSections().size(),
                savedProblem.getProperties().size(),
                savedProblem.getSampleTestCases().size());

        return problemMapper.toDto(savedProblem);
    }*/
}

