package com.xjudge.service.scraping.atcoder;

import com.xjudge.exception.XJudgeException;
import com.xjudge.service.scraping.codeforces.CodeforcesSubmission;
import com.xjudge.service.scraping.strategy.LoginService;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AtCoderLoginService implements LoginService {
    private static final String LOGIN_URL ="https://atcoder.jp/login?continue=https://atcoder.jp";

    @Override
    public void verifyLogin(WebDriver driver , String userName , String password){
        if(!isLogin(driver)){
            login(driver , userName , password);
        }
    }

    private void login(WebDriver driver , String userName , String password) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get(LOGIN_URL);
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

    private boolean isLogin(WebDriver driver) {
        Cookie cookie = driver.manage().getCookieNamed("REVEL_SESSION");
        return cookie != null && cookie.getValue().contains("UserScreenName") && cookie.getValue().contains("UserName");
    }
}
