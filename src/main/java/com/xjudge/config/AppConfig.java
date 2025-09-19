package com.xjudge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/** Application-wide configuration. */

@Configuration
@EnableAsync
@EnableRetry
@EnableJpaAuditing
public class AppConfig {
}
