package com.xjudge.config.stragegy;

import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.scraping.spoj.SpojScrapping;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
import com.xjudge.service.scraping.atcoder.AtCoderScrapping;
import com.xjudge.service.scraping.codeforces.CodeforcesScrapping;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ScrappingStrategyConfiguration {

    private final CodeforcesScrapping codeforcesScrapping;
    private final AtCoderScrapping atCoderScrapping;
    private final SpojScrapping spojScrapping;

    @Bean
    public Map<OnlineJudgeType, ScrappingStrategy> scrappingStrategies() {
        Map<OnlineJudgeType, ScrappingStrategy> strategies = new HashMap<>();
        strategies.put(OnlineJudgeType.codeforces, codeforcesScrapping);
        strategies.put(OnlineJudgeType.atcoder, atCoderScrapping);
        strategies.put(OnlineJudgeType.spoj, spojScrapping);
        return strategies;
    }
}
