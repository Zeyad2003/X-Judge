package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Sample;
import com.xjudge.entity.Tag;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.repository.TagRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CodeforcesGetProblem implements GetProblemAutomation {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    final String problemSetUrl = "https://codeforces.com/problemset/problem";

    @Override
    public Problem GetProblem(int contestId, char letter) {

        String problemCode = String.format("%d%c", contestId, letter);
        // Check problem stored in database or not
        Problem problem = problemRepository.getProblemByProblemCode(problemCode).orElse(null);
        if (problem != null) return problem;

        try {

            final Document document = Jsoup.connect(problemSetUrl + "/" + contestId + "/" + letter).get();

            Elements problemHeader = document.select(".header > .title");

            // Check problem is found in codeforces or not
            if (problemHeader.isEmpty()) {
                System.out.println("Problem not found");
                throw new EntityNotFoundException("PROBLEM_NOT_FOUND");
//                return null;
            }

            String problemTutorial = "https://codeforces.com/" + document.select(".roundbox.sidebox.sidebar-menu.borderTopRound > ul > li").get(1).select("a").attr("href"),
                    title = problemHeader.text().substring(3),
                    timeLimit = document.select(".header > .time-limit").text().substring(19),
                    memoryLimit = document.select(".header > .memory-limit").text().substring(21),
                    inputFile = document.select(".header > .input-file").text().substring(5),
                    outputFile = document.select(".header > .output-file").text().substring(6),
                    inputSpecification = document.select(".input-specification > p").outerHtml(),
                    outputSpecification = document.select(".output-specification > p").outerHtml(),
                    problemStatement = document.select(".problem-statement > div").get(1).outerHtml(),
                    note = document.select(".note").outerHtml();
            // String pageHtml = document.select(".problem-statement").outerHtml(); // all problem statement HTML.

            int[] problemRate = {0};

            List<Tag> tags = document.select(".tag-box")
                    .stream()
                    .filter(element -> {
                        if (!element.text().startsWith("*")) {
                            return true;
                        } else {
                            problemRate[0] = Integer.parseInt(element.text().substring(1));
                            return false;
                        }
                    }).map(element -> tagRepository.findByTagName(element.text())
                            .orElseGet(() -> tagRepository.save(new Tag(element.text())))
                    ).collect(Collectors.toList());

            List<Sample> allSamples = new ArrayList<>();
            List<Element> sampleElement = document.select(".sample-tests > .sample-test");
            for (Element sample : sampleElement) {
                List<Element> inputs = sample.select(".input > pre");
                List<Element> outputs = sample.select(".output > pre");
                for (int i = 0; i < inputs.size(); i++) {
                    Sample s = new Sample(inputs.get(i).outerHtml(), outputs.get(i).outerHtml());
                    allSamples.add(s);
                }
            }

            problem = Problem.builder()
                    .problemCode(problemCode)
                    .problemRate(problemRate[0])
                    .problemTitle(title)
                    .inputFile(inputFile)
                    .outputFile(outputFile)
                    .problemStatement(problemStatement)
                    .problemInput(inputSpecification)
                    .problemOutput(outputSpecification)
                    .problemSource("Codeforces")
                    .problemNote(note)
                    .problemTimeLimit(timeLimit)
                    .problemMemoryLimit(memoryLimit)
                    .problemTutorial(problemTutorial)
                    .problemSamples(allSamples)
                    .tags(tags)
                    .build();

            return problemRepository.save(problem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}