package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.entity.Tag;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.scraping.GetProblemAutomation;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CodeforcesGetProblem implements GetProblemAutomation {

    final String URL = "https://codeforces.com/problemset/problem";

    @Override
    public Problem GetProblem(int contestId, char letter) {

        String problemCode = OnlineJudgeType.CODEFORCES.getName() + "-" + String.format("%d%c", contestId, letter);
        final Document document;

        try {
            document = Jsoup.connect(URL + "/" + contestId + "/" + letter).get();
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

        // TODO: implement the tags list part
        List<Tag> tags = new ArrayList<>();

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
