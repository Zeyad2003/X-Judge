package com.xjudge.service.problem;

import com.xjudge.entity.problem.Problem;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.exception.BadRequestException;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProblemFetchingServiceImpl implements ProblemFetchingService {

    private final ProblemMapper problemMapper;
    private final ProblemRepository problemRepository;
    private final Map<OnlineJudgeType, ScrappingStrategy> scrappingStrategies;

    @Override
    public ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code) {
        log.info("Fetching problem: {} from {}", code, ojType);

        // Return cached if exists
        var existing = problemRepository.findByCodeAndOnlineJudge(code, ojType);
        if (existing.isPresent()) {
            log.info("Problem {} already exists in database", code);
            Problem problem = existing.get();
            return problemMapper.toDto(problem);
        }

        return scrapeAndSaveProblem(ojType, code);
    }

    /** Scrapes a new problem from the online judge and saves it to our database */
    private ProblemDetails scrapeAndSaveProblem(OnlineJudgeType ojType, String code) {
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
    }
}

