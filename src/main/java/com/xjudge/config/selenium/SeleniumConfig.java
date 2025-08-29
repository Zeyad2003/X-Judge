package com.xjudge.config.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SeleniumConfig {

    @Value("${selenium.headless:false}")
    private boolean headless;

    @Bean
    @Scope(value = "prototype")
    public WebDriver getDriver(){
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new"); // modern headless
        }

        // Linux/container-friendly flags
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Selenium Manager will download and cache a matching ChromeDriver
        return new ChromeDriver(options);
    }

}
