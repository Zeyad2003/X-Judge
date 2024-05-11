package com.xjudge.service.scraping.spoj;

import com.xjudge.service.scraping.strategy.SplitStrategy;
import org.springframework.stereotype.Service;

@Service
public class SpojSplitting implements SplitStrategy {
    @Override
    public String[] split(String code) {
        return new String[]{code};
    }
}
