package com.xjudge.service.scraping;

import com.xjudge.entity.Problem;

public interface ScrappingStrategy {
    Problem scrap(String contestId, String problemId);
}
