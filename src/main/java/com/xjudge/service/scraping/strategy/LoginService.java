package com.xjudge.service.scraping.strategy;

import org.openqa.selenium.WebDriver;

public interface LoginService {
    void verifyLogin(WebDriver driver , String username ,  String password);
}