package com.xjudge.util.driverpool;

import lombok.*;
import org.openqa.selenium.WebDriver;

@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class WebDriverWrapper {
    int id;
    WebDriver driver;
}
