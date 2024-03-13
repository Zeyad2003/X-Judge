package com.xjudge.config.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SeleniumConfig {
    @Value("${browser.binary}")
    private String browserPath;

    @Value("${browser.version}")
    private String browserVersion;
    @Bean
    @Scope(value = "prototype")
    public WebDriver getDriver(){
        WebDriverManager.chromedriver().browserVersion(browserVersion).setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // This line enables headless mode
        options.setBinary(browserPath);
        return new ChromeDriver(options);
    }

}