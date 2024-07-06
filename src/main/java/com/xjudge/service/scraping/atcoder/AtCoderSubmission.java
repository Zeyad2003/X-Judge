package com.xjudge.service.scraping.atcoder;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.scrap.SubmissionScrapedData;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import com.xjudge.util.driverpool.AtCoderPool;
import com.xjudge.util.driverpool.WebDriverWrapper;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AtCoderSubmission implements SubmissionStrategy {

    private final AtCoderPool pool;
    private static final String SUBMIT_URL="https://atcoder.jp/contests/%s/submit";
    private final AtCoderSplitting splitting;
    private static final Logger logger = LoggerFactory.getLogger(AtCoderSubmission.class);

    @Autowired
    public AtCoderSubmission(AtCoderPool atCoderPool,
                             AtCoderSplitting splitting){
        this.splitting = splitting;
        this.pool = atCoderPool;
    }


    @Override
    public Submission submit(SubmissionInfoModel data) {
        WebDriverWrapper driverWrapper = pool.getDriverData();
        WebDriver driver = driverWrapper.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Instant submitTime = Instant.now();
        try {
            String[] splittedCode = splitting.split(data.code());
            String contestId = splittedCode[0];
            String url = String.format(SUBMIT_URL, contestId);
            driver.get(url);
            submitHelper(driver, wait, data, driverWrapper);
            String remoteId = getSubmissionId(driver);
            logger.info("Remote Id : {}", remoteId);
            SubmissionScrapedData submissionScrapedData = scrapSubmissionData(driver, remoteId);
            while (submissionScrapedData.getVerdict().equalsIgnoreCase("WJ")) {
                try {
                    submissionScrapedData = scrapSubmissionData(driver, remoteId);
                    logger.info("data {}", submissionScrapedData);
                    logger.info("==================");
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
            Submission submission = setSubmissionData(submissionScrapedData, data , submitTime);
            pool.releaseDriver(driverWrapper);
            return submission;
        }
        catch (Exception e){
            pool.releaseDriver(driverWrapper);
            logger.info(e.getMessage());
            return Submission.builder()
                    .remoteRunId("0")
                    .ojType(data.ojType())
                    .solution(data.solutionCode())
                    .language(data.compiler().getName())
                    .submitTime(submitTime)
                    .memoryUsage("0 KB")
                    .timeUsage("0 ms")
                    .verdict("Waiting Judge")
                    .submissionStatus("unsubmitted")
                    .isOpen(data.isOpen() == null || data.isOpen())
                    .compiler(data.compiler())
                    .build();
        }
    }


    private String getSubmissionId(WebDriver driver){
        try {
            WebElement firstRow = driver.findElement(By.tagName("tbody")).findElement(By.tagName("tr"));
            WebElement submissionScore = firstRow.findElements(By.tagName("td")).get(4);
            return submissionScore.getAttribute("data-id");
        }catch (Exception ex){
            logger.info(ex.getMessage());
            if(ex instanceof NoSuchElementException || ex instanceof TimeoutException){
                return getSubmissionId(driver);
            }
        }
        return null;
    }

    private SubmissionScrapedData scrapSubmissionData(WebDriver driver , String remoteId){
        List<WebElement> rows = driver.findElements(By.xpath("//tbody//tr"));
        logger.info("===============");
        logger.info("total rows : {}", rows.size());
        for(WebElement row : rows){
            WebElement score = row.findElement(By.className("submission-score"));
            if(score.getAttribute("data-id").equalsIgnoreCase(remoteId)){
                List<WebElement> tds = row.findElements(By.tagName("td"));
                logger.info("Total tds : {}", tds.size());
                logger.info("verdict {}" ,tds.get(6).getText());
                if(tds.size() >= 10){
                    return  SubmissionScrapedData
                            .builder()
                            .remoteId(remoteId)
                            .time(tds.get(7).getText())
                            .memory(tds.get(8).getText())
                            .verdict(tds.get(6).getText())
                            .build();
                }
                else if(tds.get(6).getText().equals("CE")){
                    return  SubmissionScrapedData
                            .builder()
                            .remoteId(remoteId)
                            .time("0")
                            .memory("0")
                            .verdict(tds.get(6).getText())
                            .build();
                }
            }
        }
        return  SubmissionScrapedData
                .builder()
                .remoteId(remoteId)
                .time("0")
                .memory("0")
                .verdict("WJ")
                .build();
    }

    private void submitHelper(WebDriver driver , WebDriverWait wait , SubmissionInfoModel data, WebDriverWrapper driverWrapper){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("data.LanguageId")));
            Select taskNameSelect = new Select(driver.findElement(By.name("data.TaskScreenName")));
            WebElement toggleButton = driver.findElement(By.cssSelector("button.btn-toggle-editor"));
            WebElement sourceCodeArea = driver.findElement(By.name("sourceCode"));
            WebElement submitButton = driver.findElement(By.id("submit"));
            WebElement selectLang = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("select-lang-" + data.code())));
            WebElement option = selectLang.findElement(By.xpath("//select//option[@value='" + data.compiler().getIdValue() + "']"));
            WebElement selectComboBox = wait.until(ExpectedConditions.visibilityOf(selectLang.findElement(By.className("select2"))))
                    .findElement(By.className("selection")).findElement(By.tagName("span"));

            if(toggleButton.getAttribute("aria-pressed") == null) toggleButton.click();

            selectComboBox.click();
            WebElement inputSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("select2-search__field")));
            inputSearch.sendKeys(option.getText());
            inputSearch.sendKeys(Keys.ENTER);
            taskNameSelect.selectByValue(data.code());
            sourceCodeArea.sendKeys(data.solutionCode());
            submitButton.submit();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table")));
        }catch (Exception e){
            try {
                System.out.println(e.getMessage());
                if(e instanceof NoSuchElementException || e instanceof StaleElementReferenceException){
                     submitHelper(driver , wait ,data , driverWrapper);
                }
            } catch (Exception ex) {
                pool.releaseDriver(driverWrapper);
                throw new XJudgeException("FAIL_TO_SUBMIT" , AtCoderSubmission.class.getName(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private Submission setSubmissionData(SubmissionScrapedData submissionScrapedData , SubmissionInfoModel data , Instant submitTime){
        return Submission.builder()
                .remoteRunId(submissionScrapedData.getRemoteId())
                .ojType(data.ojType())
                .solution(data.solutionCode())
                .language(data.compiler().getName())
                .submitTime(submitTime)
                .memoryUsage(submissionScrapedData.getMemory())
                .timeUsage(submissionScrapedData.getTime())
                .verdict((submissionScrapedData.getVerdict().equals("AC")? "Accepted" : submissionScrapedData.getVerdict()))
                .submissionStatus("submitted")
                .isOpen(data.isOpen() == null || data.isOpen())
                .compiler(data.compiler())
                .build();
    }
}
