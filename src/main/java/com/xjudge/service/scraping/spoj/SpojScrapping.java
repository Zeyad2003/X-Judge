package com.xjudge.service.scraping.spoj;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Property;
import com.xjudge.entity.Section;
import com.xjudge.entity.Value;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.PropertyRepository;
import com.xjudge.repository.SectionRepository;
import com.xjudge.repository.ValueRepository;
import com.xjudge.service.scraping.codeforces.CodeforcesScrapping;
import com.xjudge.service.scraping.strategy.ScrappingStrategy;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpojScrapping implements ScrappingStrategy {

    private final PropertyRepository propertyRepository;
    private final SectionRepository sectionRepository;
    private final ValueRepository valueRepository;
    private final SpojSplitting spojSplitting;

    @Override
    public Problem scrap(String code) {
        String URL = "https://www.spoj.com";
        String[] splittedCode = spojSplitting.split(code);
        String problemId = splittedCode[0];
        String targetProblem = URL + "/problems/" + problemId;
        String contestName = "";
        System.out.println(targetProblem);
        Document problemDocument;

        try {
            problemDocument = Jsoup.connect(targetProblem).get();
        } catch (IOException e) {
            throw new XJudgeException("Problem not found", CodeforcesScrapping.class.getName(), HttpStatus.NOT_FOUND);
        }

        List<Property> properties = new ArrayList<>(List.of());

        Elements problemProperties = problemDocument.select("#problem-meta > tbody > tr");

        for (Element property : problemProperties) {
            Elements td = property.select("td");
            String title = td.getFirst().text();
            String content = td.get(1).text();
            if (title.contains("Resource")) contestName = content;
            properties.add(propertyRepository.save(Property.builder().title(title).content(content).build()));
        }

        String title = problemDocument.select("#problem-name").text().split("-")[1];

        Elements body = problemDocument.select("#problem-body");

        List<Pair<String, Element>> sections = getSections(body);

        List<Section> problemSections = new ArrayList<>();

        for (Pair<String, Element> section : sections) {
            String sectionTitle = section.getFirst();
            String sectionContent = section.getSecond().outerHtml();
            if (sectionTitle.contains("Example")) {
                sectionContent = generateSampleTable(section.getSecond());
            }
            Value value = valueRepository.save(Value.builder().format("HTML").content(sectionContent).build());
            problemSections.add(Section.builder().title(sectionTitle).value(value).build());
        }

        sectionRepository.saveAll(problemSections);

        return Problem.builder()
                .code(problemId)
                .onlineJudge(OnlineJudgeType.spoj)
                .title(title)
                .problemLink(targetProblem)
                .contestName(contestName)
                .contestLink(null)
                .prependHtml(getPrependHtml())
                .sections(problemSections)
                .properties(properties)
                .build();
    }

    private List<Pair<String, Element>> getSections(Elements elements) {
        List<Pair<String, Element>> sections = new ArrayList<>();
        Elements h3Elements = elements.select("h3");
        Element firstChild = elements.first().child(0);

        if (!firstChild.tagName().equals("h3")) {
            Element section = new Element("section");
            String title = "";
            for (Element sibling = firstChild; sibling != null && !sibling.tagName().equals("h3"); sibling = sibling.nextElementSibling()) {
                section.appendChild(sibling.clone());
            }
            sections.add(Pair.of(title, section));
        }

        for (Element h3 : h3Elements) {
            Element section = new Element("section");
            String title = h3.text();
            for (Element sibling = h3.nextElementSibling(); sibling != null && !sibling.tagName().equals("h3"); sibling = sibling.nextElementSibling()) {
                section.appendChild(sibling.clone());
            }
            sections.add(Pair.of(title, section));
        }
        return sections;
    }

    private String generateSampleTable(Element sample) {
        return sample.outerHtml();
    }

    private String getPrependHtml() {
        return "<style type=\"text/css\">\n" +
                "    #problem-body > pre {\n" +
                "        display: block;\n" +
                "        padding: 9.5px;\n" +
                "        margin: 0 0 10px;\n" +
                "        font-size: 13px;\n" +
                "        line-height: 1.42857143;\n" +
                "        word-break: break-all;\n" +
                "        word-wrap: break-word;\n" +
                "        color: #333;\n" +
                "        background: rgba(255, 255, 255, 0.5);\n" +
                "        border: 1px solid #ccc;\n" +
                "        border-radius: 6px;\n" +
                "    }\n" +
                "</style>";
    }
}
