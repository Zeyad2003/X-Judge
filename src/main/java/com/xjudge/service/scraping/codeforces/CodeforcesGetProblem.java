package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.entity.Tag;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.scraping.GetProblemAutomation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeforcesGetProblem implements GetProblemAutomation {

    @Override
    public Problem GetProblem(String contestId, String problemId) {

        String problemCode = OnlineJudgeType.CODEFORCES.getName() + "-" + contestId + problemId;
        final Document document;

        try {
            String URL = "https://codeforces.com/problemset/problem";
            document = Jsoup.connect(URL + "/" + contestId + "/" + problemId).get();
        } catch (IOException e) {
            throw new XJudgeException("PROBLEM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        Elements problemHeader = document.select(".header > .title");

        if (problemHeader.isEmpty()) {
            System.out.println("Problem not found");
            throw new XJudgeException("PROBLEM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        String problemTutorial = "https://codeforces.com/" +
                document.select(".roundbox.sidebox.sidebar-menu.borderTopRound > ul > li")
                        .get(1)
                        .select("a")
                        .attr("href");

        String title = problemHeader.text().substring(3);
        String timeLimit = document.select(".header > .time-limit").text().substring(19);
        String memoryLimit = document.select(".header > .memory-limit").text().substring(21);
        String inputFile = document.select(".header > .input-file").text().substring(5);
        String outputFile = document.select(".header > .output-file").text().substring(6);
        String inputSpecification = document.select(".input-specification > p").outerHtml();
        String outputSpecification = document.select(".output-specification > p").outerHtml();
        String problemStatement = document.select(".problem-statement > div").get(1).outerHtml();
        String note = document.select(".note").outerHtml();

        List<Tag> tags = document.select(".tag-box").stream()
                .map(tag -> Tag.builder().tagName(tag.text()).build())
                .collect(Collectors.toList());

        List<Sample> allSamples = new ArrayList<>();
        List<Element> sampleElement = document.select(".sample-tests > .sample-test");
        for (Element sample : sampleElement) {
            List<Element> inputs = sample.select(".input > pre");
            List<Element> outputs = sample.select(".output > pre");
            for (int i = 0; i < inputs.size(); i++) {
                Sample s = Sample.builder()
                        .sampleInput(inputs.get(i).outerHtml())
                        .sampleOutput(outputs.get(i).outerHtml())
                        .build();
                allSamples.add(s);
            }
        }

        return Problem.builder()
                .problemCode(problemCode)
                .problemTitle(title)
                .inputMethod(inputFile)
                .outputMethod(outputFile)
                .problemStatement(problemStatement)
                .problemInput(inputSpecification)
                .problemOutput(outputSpecification)
                .problemSource(OnlineJudgeType.CODEFORCES.getName())
                .problemNote(note)
                .problemTimeLimit(timeLimit)
                .problemMemoryLimit(memoryLimit)
                .problemTutorial(problemTutorial)
                .problemSamples(allSamples)
                .tags(tags)
                .build();
    }
}
