package com.xjudge.service.scraping.codeforces;

import com.xjudge.exception.XJudgeException;
import com.xjudge.service.scraping.strategy.LoginService;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CodeforcesLoginService implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(CodeforcesLoginService.class);
    private static final String SUBMIT_URL="https://codeforces.com/problemset/submit";
    private static final String LOGIN_URL="https://codeforces.com/enter?back=/problemset/submit";

    public void verifyLogin(WebDriver driver , String USERNAME , String PASSWORD) {
        if (!isLogin(driver)) login(driver , USERNAME , PASSWORD);
        else driver.get(SUBMIT_URL);
    }

    private void login(WebDriver driver , String USERNAME , String PASSWORD) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get(LOGIN_URL);
            WebElement userName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("handleOrEmail")));
            WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("submit")));
            WebElement rememberMe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remember")));
            userName.sendKeys(USERNAME);
            password.sendKeys(PASSWORD);
            rememberMe.click();
            loginButton.submit();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            if(exception instanceof NoSuchElementException || exception instanceof TimeoutException || exception instanceof StaleElementReferenceException){
                login(driver , USERNAME , PASSWORD);
            }
            throw new XJudgeException("FAIL TO LOGIN", CodeforcesSubmission.class.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isLogin(WebDriver driver) {
        Cookie cookie = driver.manage().getCookieNamed("X-User-Sha1");
        return cookie != null && !cookie.getValue().isEmpty();
    }
}
