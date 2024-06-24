package com.xjudge.service.scraping.atcoder;

import com.xjudge.entity.Submission;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.scrap.SubmissionScrapedData;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import com.xjudge.service.scraping.codeforces.CodeforcesSubmission;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

@Service
public class AtCoderSubmission implements SubmissionStrategy {

    WebDriver driver;
    WebDriverWait wait;
    private static final String LOGIN_URL = "https://atcoder.jp/login?continue=https://atcoder.jp/contests/%s/submit";
    private static final String SUBMIT_URL="https://atcoder.jp/contests/%s/submit";
    private final AtCoderSplitting splitting;
    private static final Logger logger = LoggerFactory.getLogger(AtCoderSubmission.class);
    @Value("${Atcoder.username}")
    private String USERNAME;
    @Value("${Atcoder.password}")
    private String PASSWORD;
    private final String SUBMISSION_SCORE_XPATH="/table/tbody/tr[1]/td[5]";

    @Autowired
    public AtCoderSubmission(WebDriver webDriver,
                             AtCoderSplitting splitting){
        this.driver = webDriver;
        this.wait = new WebDriverWait(webDriver , Duration.ofSeconds(10));
        this.splitting = splitting;
    }

    @PreDestroy
    public void destroyObject(){
        driver.quit();
    }

    @Override
    public Submission submit(SubmissionInfoModel data) {
        String[] splittedCode = splitting.split(data.code());
        String contestId = splittedCode[0];
        verifyLogin(contestId);
        submitHelper(data);
//        WebElement submissionScore = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SUBMISSION_SCORE_XPATH)));
        WebElement submissionScore = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("submission-score")));
        String remoteId = submissionScore.getAttribute("data-id");
        logger.info("Remote Id : {}", remoteId);
        SubmissionScrapedData submissionScrapedData = scrapSubmissionData(remoteId);
        while(submissionScrapedData.getVerdict().equalsIgnoreCase("WJ")){
            try {
                submissionScrapedData = scrapSubmissionData(remoteId);
                logger.info("data {}", submissionScrapedData);
                logger.info("==================");
            }
            catch (Exception e){
                logger.info(e.getMessage());
            }
        }
        return Submission.builder()
                .remoteRunId(remoteId)
                .ojType(data.ojType())
                .solution(data.solutionCode())
                .language(data.compiler().getName())
                .submitTime(Instant.now())
                .memoryUsage(submissionScrapedData.getMemory())
                .timeUsage(submissionScrapedData.getTime())
                .verdict((submissionScrapedData.getVerdict().equals("AC")? "Accepted" : submissionScrapedData.getVerdict()))
                .submissionStatus("submitted")
                .isOpen(data.isOpen() == null || data.isOpen())
                .build();
    }

    private SubmissionScrapedData scrapSubmissionData(String remoteId){
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

    private void submitHelper(SubmissionInfoModel data){
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
                if(e.getMessage().startsWith("Unable to locate element")) submitHelper(data);
                TimeUnit.MILLISECONDS.sleep(4000);
            } catch (InterruptedException ex) {
                throw new XJudgeException("FAIL_TO_SUBMIT" , AtCoderSubmission.class.getName(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
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

    private void verifyLogin(String contestId){
        if(!isLogin()){
            login(USERNAME , PASSWORD , contestId);
        }else{
            String url = String.format(SUBMIT_URL , contestId);
            driver.get(url);
        }
    }

}
