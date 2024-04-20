package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.service.sample.SampleService;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.util.Pair;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodeforcesGetProblem implements GetProblemAutomation {

    private final SampleService sampleService;

    @Override
    public Problem GetProblem(String contestId, String problemId) {
        String URL = "https://codeforces.com";
        String targetProblem = URL + "/problemset/problem/" + contestId + "/" + problemId;
        String contestLink = URL + "/contest/" + contestId;
        Document problemDocument;

        try {
            problemDocument = Jsoup.connect(targetProblem).get();
        } catch (IOException e) {
            throw new XJudgeException("CodeForces may be down at the current time", CodeforcesGetProblem.class.getName(), HttpStatus.NOT_FOUND);
        }

        Elements problemHeader = problemDocument.select(".header > .title");

        if (problemHeader.isEmpty()) {
            throw new XJudgeException("The Problem may be Deleted", CodeforcesGetProblem.class.getName(), HttpStatus.NOT_FOUND);
        }

        // Common Info
        String title = problemHeader.text().substring(3);
        String timeLimit = problemDocument.select(".header > .time-limit").text().substring(19);
        String memoryLimit = problemDocument.select(".header > .memory-limit").text().substring(21);

        String problemCode = contestId + problemId;

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
        String contestName = problemDocument.select(".rtable > tbody > tr > th > a").getFirst().text();
        List<Pair<String, String>> announcements = new ArrayList<>(), tutorials = new ArrayList<>();

        Elements contestMaterial = problemDocument.select(".roundbox.sidebox.sidebar-menu.borderTopRound > ul > li");
        for (Element element : contestMaterial) {
            String text = element.text(), link = element.select("a").attr("href");
            String titleAnnouncement = element.select("a").attr("title");
            if (text.contains("Announcement")) {
                announcements.add(new Pair<>(titleAnnouncement, URL + link));
            } else if (text.contains("Tutorial")) {
                tutorials.add(new Pair<>(titleAnnouncement, URL + link));
            }
        }

        List<Sample> samplesList = new ArrayList<>();
        var samples = problemDocument.select(".sample-tests > .sample-test");
        for (var sample : samples) {
            var inputs = sample.select(".input > pre");
            var outputs = sample.select(".output > pre");
            for (int i = 0; i < inputs.size(); i++) {
                StringBuilder inputText = new StringBuilder();
                Elements divsInPre = inputs.get(i).select("div");
                if (divsInPre.isEmpty()) {
                    inputText.append(outputs.get(i).outerHtml());
                } else {
                    inputText.append("<pre>");
                    for (var div : divsInPre) {
                        inputText.append(div.text()).append("<br>");
                    }
                    inputText.append("</pre>");
                    System.out.println("The pre element contains div elements.");
                }
                Sample s = new Sample(0L, inputText.toString(), outputs.get(i).outerHtml());
                samplesList.add(s);
            }
        }
        samplesList = sampleService.saveAll(samplesList);

        Map<String, Object> extraInfo = Map.of(
                "tutorialHtml", tutorialHtml,
                "inputMethod", inputMethod,
                "outputMethod", outputMethod,
                "noteSection", noteSection,
                "problemTags", problemTags,
                "sampleTests", sampleTests,
                "contestName", contestName,
                "announcements", announcements,
                "tutorials", tutorials
        );

        return Problem.builder()
                .source(OnlineJudgeType.CodeForces)
                .title(title)
                .problemCode(problemCode)
                .problemLink(targetProblem)
                .contestLink(contestLink)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .samples(samplesList)
                .input(inputSpecification)
                .output(outputSpecification)
                .statement(problemStatement)
                .extraInfo(extraInfo)
                .build();
    }
}
