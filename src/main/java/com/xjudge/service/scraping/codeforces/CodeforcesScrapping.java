package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.*;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.PropertyRepository;
import com.xjudge.repository.SectionRepository;
import com.xjudge.repository.ValueRepository;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
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

@Service
@RequiredArgsConstructor
public class CodeforcesScrapping implements ScrappingStrategy {

    private final PropertyRepository propertyRepository;
    private final SectionRepository sectionRepository;
    private final ValueRepository valueRepository;

    @Override
    public Problem scrap(String code) {
        String URL = "https://codeforces.com";
        String contestId = code.replaceAll("(\\d+).*", "$1");
        String problemId = code.replaceAll("\\d+(.*)", "$1");
        String targetProblem = URL + "/problemset/problem/" + contestId + "/" + problemId;
        String contestLink = URL + "/contest/" + contestId;
        Document problemDocument;

        try {
            problemDocument = Jsoup.connect(targetProblem).get();
        } catch (IOException e) {
            throw new XJudgeException("Problem not found", CodeforcesScrapping.class.getName(), HttpStatus.NOT_FOUND);
        }

        Elements htmlSections = problemDocument.select(".problem-statement > div");
        String problemTitle = htmlSections.getFirst().select(".title").text().split("\\.")[1].substring(1);
        String contestName = problemDocument.select(".rtable > tbody > tr > th > a").getFirst().text();

        List<Property> properties = List.of(
                Property.builder().title("Time Limit").content(htmlSections.getFirst().select(".time-limit").text().substring(20)).spoiler(false).build(),
                Property.builder().title("Memory Limit").content(htmlSections.getFirst().select(".memory-limit").text().substring(22)).spoiler(false).build(),
                Property.builder().title("Input").content(htmlSections.getFirst().select(".input-file").text().substring(6)).spoiler(false).build(),
                Property.builder().title("Output").content(htmlSections.getFirst().select(".output-file").text().substring(7)).spoiler(false).build()
        );

        propertyRepository.saveAll(properties);

        List<Section> problemSections = new ArrayList<>();
        for (int i = 1; i < htmlSections.size(); i++) {
            String title = htmlSections.get(i).select(".section-title").text();
            String content = htmlSections.get(i).children().not(".section-title").outerHtml();
            if (title.contains("Example")) {
                content = generateSampleTable(htmlSections.get(i));
            }
            Value value = valueRepository.save(Value.builder().format("HTML").content(content).build());
            problemSections.add(Section.builder().title(title).value(value).build());
        }

        sectionRepository.saveAll(problemSections);

        return Problem.builder()
                .code(contestId+problemId)
                .onlineJudge(OnlineJudgeType.codeforces)
                .title(problemTitle)
                .contestName(contestName)
                .problemLink(targetProblem)
                .contestLink(contestLink)
                .prependHtml(getPrependHtml())
                .sections(problemSections)
                .properties(properties)
                .build();
    }

    private String generateSampleTable(Element sample) {
        List<Sample> samples = getSamples(sample);
        StringBuilder HtmlContent = new StringBuilder();
        for (Sample s : samples) {
            String table = "<div class=\"sampleTests ps-5 pe-5\">\n" +
                    "    <table class=\"table table-bordered sample\">\n" +
                    "        <thead>\n" +
                    "            <tr style=\"background-color:#ebebeb\">\n" +
                    "                <th class=\"w-50\">Input</th>\n" +
                    "                <th class=\"w-50\">Output</th>\n" +
                    "            </tr>\n" +
                    "        </thead>\n" +
                    "        <tbody>\n" +
                    "            <tr>\n" +
                    "                <td class=\"text-start\"> " + s.getInput() + " </td>\n" +
                    "                <td class=\"text-start\"> " + s.getOutput() + " </td>\n" +
                    "            </tr>\n" +
                    "        </tbody>\n" +
                    "    </table>\n" +
                    "</div>\n";
            HtmlContent.append(table);
        }
        return HtmlContent.toString();
    }

    private List<Sample> getSamples(Element samples) {
        List<Sample> samplesList = new ArrayList<>();
        Elements samplesIterators = samples.select(".sample-tests > .sample-test");
        for (var sample : samplesIterators) {
            var inputs = sample.select(".input > pre");
            var outputs = sample.select(".output > pre");
            for (int i = 0; i < inputs.size(); i++) {
                StringBuilder inputText = new StringBuilder();
                Elements divsInPre = inputs.get(i).select("div");
                if (divsInPre.isEmpty()) {
                    inputText.append(inputs.get(i).outerHtml());
                } else {
                    inputText.append("<pre>");
                    for (var div : divsInPre) {
                        inputText.append(div.text()).append("<br>");
                    }
                    inputText.append("</pre>");
                }
                Sample s = new Sample(inputText.toString(), outputs.get(i).outerHtml());
                samplesList.add(s);
            }
        }
        return samplesList;
    }

    private String getPrependHtml() {
        return """
                <!-- MathJax -->
                    <script type="text/x-mathjax-config" th:inline="none">
                        MathJax.Hub.Config({
                          tex2jax: {inlineMath: [['$$$','$$$']], displayMath: [['$$$$$$','$$$$$$']]}
                        });
                    </script>
                    <script type="text/javascript" async
                            src="https://mathjax.codeforces.org/MathJax.js?config=TeX-AMS_HTML-full">
                    </script>
                    <!-- /MathJax -->
                """;
    }

}