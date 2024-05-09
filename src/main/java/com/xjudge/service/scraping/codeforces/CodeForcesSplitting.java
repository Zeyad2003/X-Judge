package com.xjudge.service.scraping.codeforces;

import com.xjudge.service.scraping.strategy.SplitStrategy;
import org.springframework.stereotype.Service;

@Service
public class CodeForcesSplitting implements SplitStrategy {
    @Override
    public String[] split(String code) {
        String contestId = code.replaceAll("(\\d+).*", "$1");
        String problemId = code.replaceAll("\\d+(.*)", "$1");
        return new String[]{contestId, problemId};
    }
}
