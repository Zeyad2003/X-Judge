package com.xjudge.util.driverpool;

import com.xjudge.service.scraping.atcoder.AtCoderLoginService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AtCoderPool implements DriverPool{
    private final Map<WebDriverWrapper, DriverSessionData> pool;
    private final WebDriver driver1;
    private final WebDriver driver2;
    private final Logger logger = LoggerFactory.getLogger(CodeforcesPool.class);
    private int numberOfBusyDriver;
    private final AtCoderLoginService atCoderLoginService;
    @Value("${Atcoder.username1}")
    private String USERNAME1;
    @Value("${Atcoder.password1}")
    private String PASSWORD1;
    @Value("${Atcoder.username2}")
    private String USERNAME2;
    @Value("${Atcoder.password2}")
    private String PASSWORD2 ;

    @Autowired
    public AtCoderPool(WebDriver driver1, WebDriver driver2  , AtCoderLoginService atCoderLoginService){
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.atCoderLoginService = atCoderLoginService;
        pool = new HashMap<>();
    }

    @PostConstruct
    public void initializeDrivers(){
        atCoderLoginService.verifyLogin(driver1 , USERNAME1 , PASSWORD1);
        atCoderLoginService.verifyLogin(driver2 , USERNAME2 , PASSWORD2);
        pool.put(new WebDriverWrapper(1 , driver1) , new DriverSessionData(USERNAME1 , PASSWORD1 , false));
        pool.put(new WebDriverWrapper(2 , driver2) , new DriverSessionData(USERNAME2 , PASSWORD2 , false));
    }

    @PreDestroy
    public void preDestroy(){
        for(WebDriverWrapper driver : pool.keySet()){
            driver.getDriver().quit();
        }
    }

    @SneakyThrows
    @Override
    public synchronized WebDriverWrapper getDriverData() {
        logger.info("The number of driver in pool : {}" , pool.size());
        logger.info("The number of Busy driver in pool : {}" , numberOfBusyDriver);
        logger.info("The number of free driver in pool : {}" , pool.size() - numberOfBusyDriver);
        if(findFreeDriver() == null){
            WebDriverWrapper driverWrapper = findFreeDriver();
            while (driverWrapper == null){
                driverWrapper = findFreeDriver();
            }
            electDriver(driverWrapper);
            return driverWrapper;
        }
        WebDriverWrapper driverWrapper = findFreeDriver();
        electDriver(driverWrapper);
        return driverWrapper;
    }

    @Override
    public void releaseDriver(WebDriverWrapper data){
        DriverSessionData metaData = pool.get(data);
        metaData.setDriverActive(false);
        pool.put(data , metaData);
        numberOfBusyDriver--;
    }

    private WebDriverWrapper findFreeDriver(){
        for(WebDriverWrapper driverData : pool.keySet()){
            if(!pool.get(driverData).isDriverActive()){
                return driverData;
            }
        }
        return null;
    }

    private void electDriver(WebDriverWrapper data){
        DriverSessionData metaData = pool.get(data);
        atCoderLoginService.verifyLogin(data.getDriver() , metaData.getUserName(),  metaData.getPassword());
        metaData.setDriverActive(true);
        pool.put(data , metaData);
        numberOfBusyDriver++;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    private static class DriverSessionData{
        String userName;
        String password;
        boolean driverActive;
    }
}
