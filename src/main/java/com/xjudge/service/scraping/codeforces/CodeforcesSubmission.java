package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.scrap.SubmissionScrapedData;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import com.xjudge.util.driverpool.CodeforcesPool;
import com.xjudge.util.driverpool.WebDriverWrapper;
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

@Service
public class CodeforcesSubmission implements SubmissionStrategy {
    private final CodeforcesPool driverPool;
    private static final Logger logger = LoggerFactory.getLogger(CodeforcesSubmission.class);

    @Autowired
    public CodeforcesSubmission(CodeforcesPool codeForcesPool) {
        this.driverPool = codeForcesPool;
    }

    @Override
    public Submission submit(SubmissionInfoModel info) {
        WebDriverWrapper driverWrapper = driverPool.getDriverData();
        WebDriver driver= driverWrapper.getDriver();
        WebDriverWait wait = new WebDriverWait(driver , Duration.ofSeconds(10));
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("submit-form")));
            submitHelper(driver , wait ,info , driverWrapper);

            String id = getSubmissionId(driver);
            SubmissionScrapedData data = scrapSubmissionResult(driver , wait , id);
            while (data == null || data.getVerdict().contains("queue") || data.getVerdict().contains("Running")) {
                try {
                    data = scrapSubmissionResult(driver , wait , id);
                }catch (Exception e){
                    logger.info(e.getMessage());
                }
            }
            Submission submission = setSubmissionData(data , info);
            driverPool.releaseDriver(driverWrapper);
            return submission;
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            driverPool.releaseDriver(driverWrapper);
            throw new XJudgeException(exception.getMessage(), CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private SubmissionScrapedData scrapSubmissionResult(WebDriver driver , WebDriverWait wait , String remoteId) {
//        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("status-frame-datatable")));
        List<WebElement> rows = driver.findElements(By.className("highlighted-row"));
        WebElement submissionRow = rows.stream()
                .filter(row -> row.getText().contains(remoteId))
                .findFirst()
                .orElseGet(()->null);
        return(submissionRow != null)? SubmissionScrapedData
                .builder()
                .remoteId(remoteId)
                .time(submissionRow.findElement(By.className("time-consumed-cell")).getText())
                .memory(submissionRow.findElement(By.className("memory-consumed-cell")).getText())
                .verdict(submissionRow.findElement(By.className("status-cell")).getText())
                .build() : null;
    }


    private String getSubmissionId(WebDriver driver){
        String id = null;
        while(id == null){
            try {
                id = scrapSubmissionId(driver);
            }
            catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        return id;
    }


    private String scrapSubmissionId(WebDriver driver){
        WebElement remoteId = driver.findElement(By.className("id-cell"));
        return remoteId.getText();
    }


    private void submitHelper(WebDriver driver , WebDriverWait wait , SubmissionInfoModel info , WebDriverWrapper driverWrapper) {
        try {
            // get submission elements
            WebElement submittedProblemCode = driver.findElement(By.name("submittedProblemCode"));
            Select lang = new Select(driver.findElement(By.name("programTypeId")));
            WebElement singlePageSubmitButton = driver.findElement(By.id("singlePageSubmitButton"));
            WebElement toggleEditorCheckbox = driver.findElement(By.id("toggleEditorCheckbox"));
            WebElement sourceCodeTextarea = driver.findElement(By.id("sourceCodeTextarea"));
            logger.info(info.solutionCode());
            submittedProblemCode.sendKeys(info.code());
            if (!toggleEditorCheckbox.isSelected()) toggleEditorCheckbox.click();
            sourceCodeTextarea.sendKeys(info.solutionCode());
            lang.selectByValue(info.compiler().getIdValue());
            singlePageSubmitButton.submit();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("status-frame-datatable")));
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            if(exception instanceof NoSuchElementException || exception instanceof TimeoutException || exception instanceof StaleElementReferenceException){
                submitHelper(driver , wait , info , driverWrapper);
            }
            else{
                checkAlert(driver , wait , driverWrapper);
            }

        }
    }

    private void checkAlert(WebDriver driver , WebDriverWait wait ,WebDriverWrapper driverWrapper){
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shiftUp")));
            driverPool.releaseDriver(driverWrapper);
            throw new XJudgeException(webElement.getText(), CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception exception2) {
            logger.info(exception2.getMessage());
            driverPool.releaseDriver(driverWrapper);
            throw new XJudgeException("Fail to submit", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Submission setSubmissionData(SubmissionScrapedData data , SubmissionInfoModel info){
        return Submission.builder()
                .remoteRunId(data.getRemoteId())
                .ojType(info.ojType())
                .solution(info.solutionCode())
                .language(info.compiler().getName())
                .submitTime(Instant.now())
                .memoryUsage(data.getMemory())
                .timeUsage(data.getTime())
                .verdict(data.getVerdict())
                .submissionStatus("submitted")
                .isOpen(info.isOpen() == null || info.isOpen())
                .build();
    }

}
