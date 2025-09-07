package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemDetails;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.codeforces.CodeforcesProblemScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProblemFetchingService implements ProblemFetchingInterface {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;
    private final CodeforcesProblemScraper scraper;

    @Override
    @Transactional
    public ProblemDetails fetchByLink(String link) {
//        var parsed = urlParser.parse(link);
//        String code = parsed.contestId() + parsed.index();
//
//        return problemRepository.findByCodeAndOnlineJudge(code, OnlineJudgeType.codeforces)
//                .map(problemMapper::toDetails)
//                .orElseGet(() -> {
//                    ProblemDetails details = scraper.scrape(parsed.contestId(), parsed.index());
//                    Problem entity = problemMapper.toEntity(details);
//                    entity = problemRepository.save(entity);
//                    return problemMapper.toDetails(entity);
//                });
        return null;
    }

    @Override
    public ProblemDetails fetchByOriginAndCode(OnlineJudgeType ojType, String code) {

        Map<String, Object> section = Map.of(
                "id", 1,
                "name", "Section 1",
                "description", "A sample section",
                "meta", Map.of("difficulty", "easy", "tags", List.of("math", "dp"))
        );
        Map<String, Object> sample = Map.of(
                "input", "{\"numbers\":[1,2,3]}",
                "output", "{\"result\":6}"
        );

        Problem problem = Problem.builder()
                .onlineJudge(ojType)
                .code(code)
                .title("Sample Problem")
                .contestName("This is a sample problem description.")
                .problemUrl("https://example.com/problem/" + code)
                .contestUrl("https://example.com/contest/" + code)
                .sections(section)
                .samples(sample)
                .constraints("Time Limit: 1000 ms\nMemory Limit: 65536 KB")
                .build();
        problem = problemRepository.save(problem);
        return problemMapper.toDetails(problem);
    }

}
