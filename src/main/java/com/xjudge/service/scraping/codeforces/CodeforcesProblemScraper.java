package com.xjudge.service.scraping.codeforces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.xjudge.entity.problem.Problem;
import com.xjudge.entity.problem.Property;
import com.xjudge.entity.problem.SampleTestCase;
import com.xjudge.entity.problem.Section;
import com.xjudge.exception.BadRequestException;
import com.xjudge.exception.NetworkScrapingException;
import com.xjudge.exception.NotFoundException;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.enums.SectionFormat;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scrapes problem details from Codeforces.
 * Example problem URL: <a href="https://codeforces.com/problemset/problem/1000/A">...</a>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeforcesProblemScraper implements ScrappingStrategy {

    private static final String BASE_URL = "https://codeforces.com";
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"
                    + " Chrome/119.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)"
                    + " Chrome/119.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko)"
                    + " Version/17.1 Safari/605.1.15");

    private static final int CONNECTION_TIMEOUT = 15000;

    private static final Pattern CODE_PATTERN = Pattern.compile("^(\\d+)([A-Za-z0-9]+)$");
    private static final Random random = new Random();

    @Override
    public Problem scrap(String code) {
        String[] parts = splitProblemCode(code);
        String contestId = parts[0];
        String problemIndex = parts[1];

        Document doc = fetchProblemPage(contestId, problemIndex);
        Element problemStatement = doc.selectFirst(".problem-statement");

        if (problemStatement == null) {
            throw new NotFoundException("There's no Problem with this origin and code.");
        }

        String rawTitle = safeText(problemStatement.selectFirst(".title"));

        String title = rawTitle.replaceFirst("^[A-Z0-9]+\\.\\s*", "");
        String contestName = safeText(doc.selectFirst("table.rtable th a"));
        String problemUrl = BASE_URL + "/problemset/problem/" + contestId + "/" + problemIndex;
        String contestUrl = BASE_URL + "/contest/" + contestId;

        List<Property> properties = extractProperties(problemStatement);
        List<Section> sections = extractSections(problemStatement);
        List<SampleTestCase> samples = extractSampleTestCases(problemStatement);

        Map<String, Object> extraMeta = extractExtraMetadata(doc);

        Problem problem = Problem.builder()
                .code(code)
                .onlineJudge(OnlineJudgeType.codeforces)
                .fetchingStatus(FetchingStatus.SUCCESSFUL)
                .title(title)
                .contestName(contestName)
                .problemUrl(problemUrl)
                .contestUrl(contestUrl)
                .extraMetadata(extraMeta)
                .build();

        for (Property property : properties)
            property.setProblem(problem);
        for (Section section : sections)
            section.setProblem(problem);
        for (SampleTestCase sample : samples)
            sample.setProblem(problem);

        problem.setProperties(properties);
        problem.setSections(sections);
        problem.setSampleTestCases(samples);

        return problem;
    }

    private String[] splitProblemCode(String code) {
        String c = code == null ? "" : code.trim();
        Matcher matcher = CODE_PATTERN.matcher(c);
        if (!matcher.matches()) {
            throw new BadRequestException("Invalid Codeforces problem code format: " + code);
        }
        return new String[]{matcher.group(1), matcher.group(2).toUpperCase()};
    }

    private Document fetchProblemPage(String contestId, String problemIndex) {
        String url = String.format("%s/problemset/problem/%s/%s", BASE_URL, contestId, problemIndex);
        try {
            return Jsoup.connect(url)
                    .userAgent(getRandomUserAgent())
                    .timeout(CONNECTION_TIMEOUT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .get();
        } catch (HttpStatusException e) {
            throw new NotFoundException("Received non-OK status code " + e.getStatusCode() + " from " + url, e);
        } catch (IOException e) {
            throw new NetworkScrapingException("A network error occurred while fetching problem from " + url, e);
        }
    }

    private static String getRandomUserAgent() {
        return USER_AGENTS.get(random.nextInt(USER_AGENTS.size()));
    }

    private String safeText(Element e) {
        return e == null ? "" : e.text().trim();
    }

    // Extracts properties: time limit, memory limit, input/output
    private List<Property> extractProperties(Element statement) {
        List<Property> props = new ArrayList<>();

        Element timeLimit = statement.selectFirst(".time-limit");
        Element memoryLimit = statement.selectFirst(".memory-limit");
        Element inputFile = statement.selectFirst(".input-file");
        Element outputFile = statement.selectFirst(".output-file");

        if (timeLimit != null) {
            props.add(Property.builder()
                    .title("Time Limit")
                    .content(parseTimeLimit(timeLimit.ownText()))
                    .build());
        }

        if (memoryLimit != null) {
            props.add(Property.builder()
                    .title("Memory Limit")
                    .content(parseMemoryLimit(memoryLimit.ownText()))
                    .build());
        }

        if (inputFile != null) {
            props.add(Property.builder()
                    .title("Input")
                    .content(inputFile.ownText().trim())
                    .build());
        }

        if (outputFile != null) {
            props.add(Property.builder()
                    .title("Output")
                    .content(outputFile.ownText().trim())
                    .build());
        }

        return props;
    }

    private String parseTimeLimit(String text) {
        if (text.contains("second")) {
            double seconds = Double.parseDouble(text.replaceAll("[^0-9.]", ""));
            return (int) (seconds * 1000) + " ms";
        }
        return text.trim();
    }

    private String parseMemoryLimit(String text) {
        if (text.contains("megabyte")) {
            double mb = Double.parseDouble(text.replaceAll("[^0-9.]", ""));
            return (int) (mb * 1024) + " kb";
        }
        return text.trim();
    }

    // Extracts main sections using section-title, statement, notes
    private List<Section> extractSections(Element statement) {
        List<Section> sections = new ArrayList<>();
        Elements divs = statement.children();
        int order = 1;
        for (Element div : divs) {
            if (div.hasClass("header"))
                continue;
            Element sectionTitle = div.selectFirst(".section-title");
            String title = safeText(sectionTitle);
            log.info("Extracting section: {}", title.isEmpty() ? "Statement" : title);
            String content = div.children().not(".section-title").outerHtml();
            log.info("Content length: {}", content.length());
            sections.add(Section.builder()
                    .title(title.isEmpty() ? "Statement" : title)
                    .sectionFormat(SectionFormat.HTML)
                    .content(content)
                    .sectionOrder(order++)
                    .build());
        }
        return sections;
    }

    // Extract sample test cases from the sample-tests block
    private List<SampleTestCase> extractSampleTestCases(Element statement) {
        List<SampleTestCase> samples = new ArrayList<>();
        Element sampleTests = statement.selectFirst(".sample-tests");
        if (sampleTests != null) {
            Elements testCases = sampleTests.select(".sample-test");
            int order = 1;
            for (Element test : testCases) {
                String input = extractTextFromPre(test.selectFirst(".input > pre"));
                String output = extractTextFromPre(test.selectFirst(".output > pre"));
                samples.add(SampleTestCase.builder()
                        .input(input)
                        .output(output)
                        .sampleOrder(order++)
                        .build());
            }
        }
        return samples;
    }

    // Support div/pre formatting in sample inputs
    private String extractTextFromPre(Element preElement) {
        if (preElement == null)
            return "";
        Elements divs = preElement.select("div");
        if (!divs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Element div : divs) {
                if (!sb.isEmpty())
                    sb.append('\n');
                sb.append(div.text());
            }
            return sb.toString();
        }
        String html = preElement.html().replaceAll("(?i)<br.*?>", "\n");
        html = html.replace("\r", "").replaceAll("(?s)<[^>]+>", "");
        return Parser.unescapeEntities(html, false);
    }

    // Extracts tags, difficulty, editorials, announcements
    private Map<String, Object> extractExtraMetadata(Document doc) {
        Map<String, Object> meta = new HashMap<>();
        Elements tags = doc.select(".tag-box");
        List<String> tagsList = new ArrayList<>();
        String difficulty = null;
        for (Element tag : tags) {
            String txt = tag.text().trim();
            if (txt.startsWith("*"))
                difficulty = txt;
            else if (!txt.isEmpty())
                tagsList.add(txt);
        }
        if (!tagsList.isEmpty())
            meta.put("tags", tagsList);
        if (difficulty != null) {
            meta.put("difficulty", difficulty);
            try {
                meta.put("difficultyRating", Integer.parseInt(difficulty.replaceFirst("^\\*", "")));
            } catch (NumberFormatException e) {
                log.warn("Failed to parse difficulty rating: {}", difficulty, e);
            }
        }

        // Editorials and announcements (example)
        List<Map<String, String>> editorials = new ArrayList<>();
        List<Map<String, String>> announcements = new ArrayList<>();
        for (Element link : doc.select("a")) {
            String href = link.absUrl("href");
            if (href.contains("/blog/entry") && link.text().toLowerCase().contains("tutorial"))
                editorials.add(Map.of("title", link.text(), "url", href));
            if (href.contains("/blog/entry") && link.text().toLowerCase().contains("announcement"))
                announcements.add(Map.of("title", link.text(), "url", href));
        }
        if (!editorials.isEmpty())
            meta.put("editorials", editorials);
        if (!announcements.isEmpty())
            meta.put("announcements", announcements);
        return meta;
    }
}
