package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.scrap.SubmissionScrapedData;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CodeforcesSubmission implements SubmissionStrategy {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(CodeforcesSubmission.class);
    @Value("${CodeForces.username}")
    private String USERNAME;
    @Value("${CodeForces.password}")
    private String PASSWORD;


    public CodeforcesSubmission(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @PreDestroy
    public void destroyObject(){
        driver.quit();
    }

    @Override
    public Submission submit(SubmissionInfoModel info) {
        try {
            verifyLogin();
            wait.until(driver -> driver.findElement(By.className("submit-form")));
            submitHelper(info);
            WebElement remoteId = driver.findElement(By.className("id-cell"));
            String id = remoteId.getText();

            SubmissionScrapedData data = scrapSubmissionResult(id);
            while (data.getVerdict().contains("queue") || data.getVerdict().contains("Running")) {
                data = scrapSubmissionResult(id);
            }

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
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new XJudgeException(exception.getMessage(), CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private SubmissionScrapedData scrapSubmissionResult(String remoteId) {
        driver.navigate().to("https://codeforces.com/problemset/status?my=on");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("status-frame-datatable")));
        List<WebElement> rows = driver.findElements(By.className("highlighted-row"));
        WebElement submissionRow = rows.stream()
                .filter(row -> row.getText().contains(remoteId))
                .findFirst()
                .orElseGet(()->null);
        return SubmissionScrapedData
                .builder()
                .remoteId(remoteId)
                .time(submissionRow.findElement(By.className("time-consumed-cell")).getText())
                .memory(submissionRow.findElement(By.className("memory-consumed-cell")).getText())
                .verdict(submissionRow.findElement(By.className("status-cell")).getText())
                .build();
    }


    private void submitHelper(SubmissionInfoModel info) {
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
            try {
                WebElement webElement = driver.findElement(By.className("shiftUp"));
                throw new XJudgeException(webElement.getText(), CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            catch (Exception exception2) {
                logger.info(exception2.getMessage());
                throw new XJudgeException("Fail to submit", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    public void login() {
        try {
            driver.get("https://codeforces.com/enter?back=/problemset/submit");
            WebElement userName = driver.findElement(By.name("handleOrEmail"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.className("submit"));
            WebElement rememberMe = driver.findElement(By.id("remember"));
            userName.sendKeys(USERNAME);
            password.sendKeys(PASSWORD);
            rememberMe.click();
            loginButton.submit();
        } catch (Exception exception) {
            logger.error("FAIL TO LOGIN");
            throw new XJudgeException("FAIL TO LOGIN", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void verifyLogin() {
        if (!isLogin()) login();
        else driver.get("https://codeforces.com/problemset/submit");
    }

    private boolean isLogin() {
        Cookie cookie = driver.manage().getCookieNamed("X-User-Sha1");
        return cookie != null && !cookie.getValue().isEmpty();
    }
}
