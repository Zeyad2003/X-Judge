package com.xjudge.util.driverpool;
public interface DriverPool {
    public WebDriverWrapper getDriverData();
    void releaseDriver(WebDriverWrapper driver);
}
