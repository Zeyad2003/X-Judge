package com.xjudge.service.scraping.atcoder;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.sample.SampleService;
import com.xjudge.service.scraping.GetProblemAutomation;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AtCoderGetProblem implements GetProblemAutomation {

    private final SampleService sampleService;

    @Override
    public Problem GetProblem(String contestId, String problemId) {
        String atCoderURL = "https://atcoder.jp/contests/";
        String targetProblem = atCoderURL + contestId + "/tasks/" + problemId;
        String contestLink = atCoderURL + contestId;
        Connection connection;
        Document problemDocument;
        try {
            connection = Jsoup.connect(targetProblem);
            problemDocument = connection.get();
        }catch (Exception e){
            throw new XJudgeException("Problem not found", AtCoderGetProblem.class.getName(), HttpStatus.NOT_FOUND);
        }

        Elements ProblemStatement = problemDocument.select("#task-statement > .lang > .lang-en > div");

        String title = problemDocument.select(".col-sm-12 .h2").getFirst().ownText().substring(4);
        String[] tmLimit = problemDocument.select(".col-sm-12").get(1).select("p").getFirst().text().split("/");
        String timeLimit = tmLimit[0].substring(11);
        String memoryLimit = tmLimit[1].substring(14);
        String contestName = problemDocument.select(".contest-title").text();

        String statement = "", constrains = "", inputSpecification = "", outputSpecification = "", story = "", scoring = "",
        inputAndOutput = "", inputGeneration = "", tools = "";
        List<Sample> samples = new ArrayList<>();

        for (Element part : ProblemStatement.select(".part")) {
            String header = part.select("h3").text();
            if (header.startsWith("Sample Input")) {
                String input = part.select("section > pre").outerHtml();
                Sample sample = new Sample(0L, input, "");
                samples.add(sample);
            } else if (header.startsWith("Sample Output")) {
                String output = part.select("section > pre").outerHtml();
                samples.getLast().setOutput(output);

                Elements elements = part.select("section *");
                elements.removeIf(el -> !(el.tagName().equals("p") || el.tagName().equals("ul")));
                Set<Element> uniqueElements = new LinkedHashSet<>(elements);
                elements.clear();
                elements.addAll(uniqueElements);
                String SampleTestNote = elements.outerHtml();
                samples.getLast().setNote(SampleTestNote);
            } else if (header.startsWith("Tools")) {
                tools = part.select("section :not(h3)").outerHtml();
            } else {
                switch (header) {
                    case "Problem Statement":
                        statement = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Constraints":
                        constrains = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Input":
                        inputSpecification = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Output":
                        outputSpecification = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Story":
                        story = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Scoring":
                        scoring = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Input and Output":
                        inputAndOutput = part.select("section :not(h3)").outerHtml();
                        break;
                    case "Input Generation":
                        inputGeneration = part.select("section :not(h3)").outerHtml();
                        break;

                }
            }
        }
        samples = sampleService.saveAll(samples);

        Map<String, Object> extraInfo = Map.of(
                "story", story,
                "scoring", scoring,
                "inputAndOutput", inputAndOutput,
                "inputGeneration", inputGeneration,
                "tools", tools,
                "constrains", constrains
        );

        return Problem.builder()
                .source(OnlineJudgeType.AtCoder)
                .title(title)
                .problemCode(problemId)
                .problemLink(targetProblem)
                .contestLink(contestLink)
                .contestName(contestName)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .samples(samples)
                .input(inputSpecification)
                .output(outputSpecification)
                .statement(statement)
                .extraInfo(extraInfo)
                .build();
    }
}
