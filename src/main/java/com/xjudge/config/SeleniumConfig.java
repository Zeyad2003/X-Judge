package com.xjudge.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {
    @Value("${brave.binary}")
    private String braveBinaryPath;

    @Bean
    public WebDriver getDriver(){
        WebDriverManager.chromedriver().browserVersion("119").setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // This line enables headless mode
        options.setBinary(braveBinaryPath);
        return new ChromeDriver(options);
    }

}