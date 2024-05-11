package com.xjudge.service.scraping.atcoder;

import com.xjudge.service.scraping.strategy.SplitStrategy;
import org.springframework.stereotype.Service;

@Service
public class AtCoderSplitting implements SplitStrategy {
    @Override
    public String[] split(String code) {
        return code.split("_");
    }
}
