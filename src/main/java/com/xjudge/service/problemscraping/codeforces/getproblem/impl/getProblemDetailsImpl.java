package com.xjudge.service.problemscraping.codeforces.getproblem.impl;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.service.problemscraping.codeforces.getproblem.getProblemDetails;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class getProblemDetailsImpl implements getProblemDetails {

    private final String problemSetUrl = "https://codeforces.com/problemset/problem";

    @Override
    public Problem Details(int contestId, char letter) {
        try {
            final Document document = Jsoup.connect(problemSetUrl + "/" + contestId + "/" + letter).get();
            var title = document.select(".header > .title").text().substring(3);
            var timeLimit = document.select(".header > .time-limit").text().substring(19);
            var memoryLimit = document.select(".header > .memory-limit").text().substring(21);
            // var pageHtml = document.select(".problem-statement").outerHtml(); // all problem statement HTML that contains description, input, output, samples, and notes
            var inputFile = document.select(".header > .input-file").text().substring(5);
            var outputFile = document.select(".header > .output-file").text().substring(6);
            var inputSpecification = document.select(".input-specification > p").outerHtml();
            var outputSpecification = document.select(".output-specification > p").outerHtml();
            var problemStatement = document.select(".problem-statement > div").get(1).outerHtml();
            List<String> tags = document.select(".tag-box")
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
            // TODO
            List<Sample> allSamples = new ArrayList<>();
            var samples = document.select(".sample-tests > .sample-test");
            for (var sample : samples) {
                var inputs = sample.select(".input > pre");
                var outputs = sample.select(".output > pre");
                for (int i = 0; i < inputs.size(); i++) {
                    Sample s = new Sample(0L, inputs.get(i).outerHtml(), outputs.get(i).outerHtml());
                    allSamples.add(s);
                }
            }
            var note = document.select(".note").outerHtml();
            Problem problem = new Problem(0L, title, problemStatement, inputFile, outputFile, "Codeforces", timeLimit, memoryLimit, "Tutorial here", allSamples, tags);
            return problem;
        } catch (Exception e) {
            System.out.println(e.getMessage()); // handle exception
        }
        return null;
    }
}
