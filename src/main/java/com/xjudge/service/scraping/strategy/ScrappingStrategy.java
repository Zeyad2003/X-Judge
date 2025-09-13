package com.xjudge.service.scraping.strategy;

import com.xjudge.entity.problem.Problem;

public interface ScrappingStrategy {
    Problem scrap(String code);
}
