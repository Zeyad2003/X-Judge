package com.xjudge.util.driverpool;

import com.xjudge.service.scraping.codeforces.CodeforcesLoginService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CodeforcesPool implements DriverPool{
    private final Map<WebDriverWrapper, DriverSessionData> pool;
    private final WebDriver driver1;
    private final WebDriver driver2;
    private final Logger logger = LoggerFactory.getLogger(CodeforcesPool.class);
    private int numberOfBusyDriver;
    private final CodeforcesLoginService codeForcesLoginService;
    @Value("${CodeForces.username1}")
    private String USERNAME1;
    @Value("${CodeForces.password1}")
    private String PASSWORD1;
    @Value("${CodeForces.username2}")
    private String USERNAME2;
    @Value("${CodeForces.password2}")
    private String PASSWORD2 ;

    @Autowired
    public CodeforcesPool(WebDriver driver1, WebDriver driver2 , CodeforcesLoginService codeForcesLoginService){
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.codeForcesLoginService = codeForcesLoginService;
        pool = new HashMap<>();
    }

    @PostConstruct
    public void initializeDrivers(){
        codeForcesLoginService.verifyLogin(driver1 , USERNAME1 , PASSWORD1);
        codeForcesLoginService.verifyLogin(driver2 , USERNAME2 , PASSWORD2);
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
        moveThrow();
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
        for(WebDriverWrapper driverWrapper : pool.keySet()){
            if(!pool.get(driverWrapper).isDriverActive()){
                return driverWrapper;
            }
        }
        return null;
    }

    private void electDriver(WebDriverWrapper data){
        DriverSessionData metaData = pool.get(data);
        codeForcesLoginService.verifyLogin(data.getDriver() , metaData.getUserName(),  metaData.getPassword());
        metaData.setDriverActive(true);
        pool.put(data , metaData);
        numberOfBusyDriver++;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    private static class DriverSessionData {
        String userName;
        String password;
        boolean driverActive;
    }

    void moveThrow(){
        for (WebDriverWrapper driverData : pool.keySet()){
            System.out.println("<====codeforces====>");
            System.out.println(driverData);
            System.out.println(pool.get(driverData));
            System.out.println("<====codeforces====>");
        }

    }

}




