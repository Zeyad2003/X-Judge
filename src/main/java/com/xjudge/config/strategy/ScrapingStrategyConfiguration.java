package com.xjudge.config.strategy;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.scraping.codeforces.CodeforcesProblemScraper;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Configures and provides a mapping between online judge types and their scraping strategies
 * (e.g. Codeforces), allowing the application to inject the correct scraper implementation.
 */

@Configuration
@RequiredArgsConstructor
public class ScrapingStrategyConfiguration {

    private final CodeforcesProblemScraper codeforcesProblemScraper;

    @Bean
    public Map<OnlineJudgeType, ScrappingStrategy> scrappingStrategies() {
        Map<OnlineJudgeType, ScrappingStrategy> strategies = new EnumMap<>(OnlineJudgeType.class);

        strategies.put(OnlineJudgeType.codeforces, codeforcesProblemScraper);

        return strategies;
    }
}
