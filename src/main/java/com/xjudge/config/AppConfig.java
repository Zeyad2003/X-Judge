package com.xjudge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/** Application-wide configuration. */

@EnableAsync
@EnableRetry
@Configuration
public class AppConfig {

}
