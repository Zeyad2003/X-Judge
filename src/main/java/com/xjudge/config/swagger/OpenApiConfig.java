package com.xjudge.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "X-Judge",
                        email = "xjudge@hotmail.com",
                        url = "https://codeforces.com/profile/X-Judge"
                ),
                description = "Virtual Judge System that can grab & submit different problems from different Online Judges." +
                        " It also supports creating contests and groups for users to compete and learn together.",
                title = "X-Judge API",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}