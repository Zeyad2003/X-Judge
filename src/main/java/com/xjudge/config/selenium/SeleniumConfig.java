package com.xjudge.config.selenium;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SeleniumConfig {
    @Bean
    @Scope(value = "prototype")
    public WebDriver getDriver(){
        FirefoxOptions options = new FirefoxOptions();
         options.addArguments("--headless"); // This line enables headless mode
        return new FirefoxDriver(options);
    }

}