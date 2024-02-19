package com.xjudge.service.scraping.codeforces;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.SubmissionAutomation;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Service
public class CodeforcesSubmission implements SubmissionAutomation {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final Logger logger = LoggerFactory.getLogger(CodeforcesSubmission.class);

    @Value("${CodeForces.username}")
    private String USERNAME;

    @Value("${CodeForces.password}")
    private String PASSWORD;


    public CodeforcesSubmission(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Override
    public Submission submit(SubmissionInfoModel info) {
        try {
            verifyLogin();

            wait.until(driver -> driver.findElement(By.className("submit-form")));

            submitHelper(info);

            // getting status of submitting
            WebElement status = driver.findElement(By.className("status-cell"));
            while (status.getText().contains("queue") || status.getText().contains("Running")) {
                status = driver.findElement(By.className("status-cell"));
            }

            WebElement time = driver.findElement(By.className("time-consumed-cell"));
            WebElement memory = driver.findElement(By.className("memory-consumed-cell"));
            WebElement remoteId = driver.findElement(By.className("id-cell"));

            logger.info(time.getText());
            logger.info(memory.getText());
            logger.info(status.getText());
            logger.info(remoteId.getText());

            return Submission.builder()
                    .remoteRunId(remoteId.getText())
                    .ojType(info.ojType())
                    .solution(info.solutionCode())
                    .language(info.compiler().getName())
                    .submitTime(Instant.now())
                    .memoryUsage(memory.getText())
                    .timeUsage(time.getText())
                    .verdict(status.getText())
                    .submissionStatus("submitted")
                    .build();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new XJudgeException(exception.getMessage(), CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

            // send info
            submittedProblemCode.sendKeys(info.problemCode());
            if (!toggleEditorCheckbox.isSelected()) toggleEditorCheckbox.click();
            sourceCodeTextarea.sendKeys(info.solutionCode());
            lang.selectByValue(info.compiler().getIdValue());
            //selectByValue(String.valueOf());
            singlePageSubmitButton.submit();

            wait.until(driver -> driver.findElement(By.className("status-frame-datatable")));

        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new XJudgeException("Fail to submit. The code may have been submitted before.", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
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
