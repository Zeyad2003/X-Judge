package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Compiler;
import com.xjudge.entity.Problem;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.scraping.GetProblemAutomation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CodeforcesGetProblem implements GetProblemAutomation {

    @Override
    public Problem GetProblem(String contestId, String problemId) {
        String URL = "https://codeforces.com";
        String targetProblem = URL + "/problemset/problem/" + contestId + "/" + problemId;
        Document problemDocument;


        try {
            problemDocument = Jsoup.connect(targetProblem).get();
        } catch (IOException e) {
            throw new XJudgeException("PROBLEM NOT FOUND", HttpStatus.NOT_FOUND);
        }

        Elements problemHeader = problemDocument.select(".header > .title");

        if (problemHeader.isEmpty()) {
            throw new XJudgeException("CAN'T FETCH THE PROBLEM", HttpStatus.NOT_FOUND);
        }

        // Common Info
        String title = problemHeader.text().substring(3);
        String problemCode = contestId + problemId;
        String timeLimit = problemDocument.select(".header > .time-limit").text().substring(19);
        String memoryLimit = problemDocument.select(".header > .memory-limit").text().substring(21);
        String inputSpecification = problemDocument.select(".input-specification > p").outerHtml();
        String outputSpecification = problemDocument.select(".output-specification > p").outerHtml();
        String problemStatement = problemDocument.select(".problem-statement > div").get(1).outerHtml();

        //Extra Info
        String tutorialHtml = problemDocument.select(".roundbox.sidebox.sidebar-menu.borderTopRound > ul > li").outerHtml();
        String inputMethod = problemDocument.select(".header > .input-file").text().substring(5);
        String outputMethod = problemDocument.select(".header > .output-file").text().substring(6);
        String noteSection = problemDocument.select(".note").outerHtml();
        String problemTags = problemDocument.select(".tag-box").outerHtml();
        String sampleTests = problemDocument.select(".sample-tests > .sample-test").html();

        // TODO: implement the compiler list fetching
        List<Compiler> compilers = new ArrayList<>();

        Map<String, Object> extraInfo = Map.of(
                "tutorialHtml", tutorialHtml,
                "inputMethod", inputMethod,
                "outputMethod", outputMethod,
                "noteSection", noteSection,
                "problemTags", problemTags,
                "sampleTests", sampleTests
        );

        return Problem.builder()
                .problemSource(OnlineJudgeType.CODEFORCES)
                .problemTitle(title)
                .problemCode(problemCode)
                .problemTimeLimit(timeLimit)
                .problemMemoryLimit(memoryLimit)
                .problemInput(inputSpecification)
                .problemOutput(outputSpecification)
                .problemStatement(problemStatement)
                .problemCompilers(compilers)
                .extraInfo(extraInfo)
                .build();
    }
}
