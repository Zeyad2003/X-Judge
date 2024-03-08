package com.xjudge.service.scraping.atcoder;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.sample.SampleService;
import com.xjudge.service.scraping.GetProblemAutomation;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AtCoderGetProblem implements GetProblemAutomation {

    private final SampleService sampleService;

    @Override
    public Problem GetProblem(String contestId, String problemId) {
        String atCoderURL = "https://atcoder.jp/contests/";
        String targetProblem = atCoderURL + contestId + "/tasks/" + problemId;
        String contestLink = atCoderURL + contestId;
        Document problemDocument;
        try {
            problemDocument = Jsoup.connect(targetProblem).get();
        }catch (Exception e){
            throw new XJudgeException("AtCoder may be down at the current time", AtCoderGetProblem.class.getName(), HttpStatus.NOT_FOUND);
        }

//        String problemHTML = problemDocument.select("#task-statement > .lang > .lang-en").outerHtml();

        Elements ProblemStatement = problemDocument.select("#task-statement > .lang > .lang-en > div");

        String title = problemDocument.select(".col-sm-12").get(1).select(".h2").text().substring(4);
        String[] tmLimit = problemDocument.select(".col-sm-12").get(1).select("p").get(0).text().split("/");
        String timeLimit = tmLimit[0].substring(11);
        String memoryLimit = tmLimit[1].substring(14);
        String statement = ProblemStatement.select(".part").get(0).select("section").outerHtml();
        String constrains = ProblemStatement.select(".part").get(1).select("section").outerHtml();
        String inputSpecification = ProblemStatement.select(".part").get(2).select("section").outerHtml();
        String outputSpecification = ProblemStatement.select(".part").get(3).select("section").outerHtml();

        List<Sample> samples = new ArrayList<>();

        for (int i = 4; i < ProblemStatement.size() - 1; i+=2) {
            String input = ProblemStatement.select(".part").get(i).select("section > pre").outerHtml();
            String output = ProblemStatement.select(".part").get(i+1).select("section > pre").outerHtml();
            Sample sample = new Sample(0L, input, output);
            samples.add(sample);
        }
        samples = sampleService.saveAll(samples);

        Map<String, Object> extraInfo = Map.of(
                "constrains", constrains
        );

        return Problem.builder()
                .source(OnlineJudgeType.AtCoder)
                .title(title)
                .problemCode(problemId)
                .problemLink(targetProblem)
                .contestLink(contestLink)
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
