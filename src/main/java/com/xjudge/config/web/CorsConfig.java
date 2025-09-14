package com.xjudge.config.web;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for the X-Judge application. Allows cross-origin requests
 * from configured
 * origins with proper security controls.
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${spring.mvc.cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
    private String allowedOrigins;

    @Value("${spring.mvc.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${spring.mvc.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${spring.mvc.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring.mvc.cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String[] origins = splitAndTrim(allowedOrigins);
        String[] methods = splitAndTrim(allowedMethods);
        String[] headers = splitAndTrim(allowedHeaders);

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(methods)
                .allowedHeaders(headers)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    private static String[] splitAndTrim(String value) {
        if (!StringUtils.hasText(value)) {
            return new String[0];
        }
        return Stream.of(value.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toArray(String[]::new);
    }
}
