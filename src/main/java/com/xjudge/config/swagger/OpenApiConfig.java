package com.xjudge.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Swagger configuration for the X-Judge API.
 */
@OpenAPIDefinition(
        info =
        @Info(
                title = "X-Judge API",
                version = "0.0.1-SNAPSHOT",
                description = "Virtual Judge API that aggregates problems from multiple online judges, with"
                        + " endpoints to browse problems and related resources.",
                contact =
                @Contact(
                        name = "X-Judge",
                        email = "xjudge@hotmail.com",
                        url = "https://codeforces.com/profile/X-Judge")))
public class OpenApiConfig {
}
