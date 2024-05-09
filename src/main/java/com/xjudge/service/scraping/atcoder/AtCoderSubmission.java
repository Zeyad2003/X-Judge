package com.xjudge.service.scraping.atcoder;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import com.xjudge.service.scraping.codeforces.CodeforcesSubmission;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AtCoderSubmission implements SubmissionStrategy {

    WebDriver driver;
    WebDriverWait wait;
    private static final String LOGIN_URL = "https://atcoder.jp/login?continue=https://atcoder.jp/contests/%s/submit";
    private static final String SUBMIT_URL="https://atcoder.jp/contests/%s/submit";
    private final AtCoderSplitting splitting;

    @Autowired
    public AtCoderSubmission(WebDriver webDriver,
                             AtCoderSplitting splitting){
        this.driver = webDriver;
        this.wait = new WebDriverWait(webDriver , Duration.ofSeconds(15));
        this.splitting = splitting;
    }

    @Value("${Atcoder.username}")
    private String USERNAME;

    @Value("${Atcoder.password}")
    private String PASSWORD;

    @Override
    public Submission submit(SubmissionInfoModel data) {
        String[] splittedCode = splitting.split(data.code());
        String contestId = splittedCode[0];

        if(!isLogin()){
            login(USERNAME , PASSWORD , contestId);
        }else{
            String url = String.format(SUBMIT_URL , contestId);
            driver.get(url);
        }

        submitHelper(data);

        List<WebElement> submission = driver.findElements(By.xpath("//tbody/tr[1]/td"));
        while(submission.size() < 10){
            submission = driver.findElements(By.xpath("//tbody/tr[1]/td"));
            try {
                WebElement statusTd = driver.findElement(By.xpath("//tbody/tr[1]/td[7]"));
                if (statusTd.getText().equals("CE")) break;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        WebElement submissionScore = driver.findElement(By.className("submission-score"));
        WebElement statusTD = driver.findElement(By.xpath("//tbody/tr[1]/td[7]"));

        String status , time , memory , remoteId;

        if(statusTD.getText().equals("CE")){
            status = "CE";
            time = "0 ms";
            memory = "0 KB";
        }
        else{
             status = submission.get(6).getText();
             time = submission.get(7).getText();
             memory = submission.get(8).getText();
        }
        remoteId = submissionScore.getAttribute("data-id");


        return Submission.builder()
                .remoteRunId(remoteId)
                .ojType(data.ojType())
                .solution(data.solutionCode())
                .language(data.compiler().getName())
                .submitTime(Instant.now())
                .memoryUsage(memory)
                .timeUsage(time)
                .verdict((status.equals("AC")? "Accepted" : status))
                .submissionStatus("submitted")
                .isOpen(data.isOpen() == null || data.isOpen())
                .build();
    }

    private void submitHelper(SubmissionInfoModel data){
        try {
            Select taskNameSelect = new Select(driver.findElement(By.name("data.TaskScreenName")));
            WebElement toggleButton = driver.findElement(By.cssSelector("button.btn-toggle-editor"));
            WebElement sourceCodeArea = driver.findElement(By.name("sourceCode"));
            WebElement submitButton = driver.findElement(By.id("submit"));

            if(toggleButton.getAttribute("aria-pressed") == null) {
                toggleButton.click();
            }

            String problemId = splitting.split(data.code())[1];

            taskNameSelect.selectByValue(problemId);
            WebElement languageIdElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("data.LanguageId")));
            Select languageIdSelect = new Select(languageIdElement);
            languageIdSelect.selectByValue(data.compiler().getIdValue());
            sourceCodeArea.sendKeys(data.solutionCode());
            submitButton.submit();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table")));

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new XJudgeException("FAIL_TO_SUBMIT" , AtCoderSubmission.class.getName(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void login(String userName , String password , String contestId) {
        String url = String.format(LOGIN_URL , contestId);
        try {
            driver.get(url);

            WebElement userNameField = driver.findElement(By.name("username"));
            WebElement userPasswordField = driver.findElement(By.name("password"));
            WebElement submitButton  = driver.findElement(By.id("submit"));

            userNameField.sendKeys(userName);
            userPasswordField.sendKeys(password);
            submitButton.submit();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert")));


        }catch (Exception e){
            throw new XJudgeException("FAIL TO LOGIN", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        WebElement alert = driver.findElement(By.className("alert"));
        if(alert.getText().contains("Contest not found")){
            throw new XJudgeException("Contest Not Found" , AtCoderSubmission.class.getName() , HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isLogin() {
        Cookie cookie = driver.manage().getCookieNamed("REVEL_SESSION");
        return cookie != null && cookie.getValue().contains("UserScreenName") && cookie.getValue().contains("UserName");
    }
}
