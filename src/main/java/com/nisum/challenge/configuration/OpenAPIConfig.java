package com.nisum.challenge.configuration;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info().title("User API")
                        .version("1.0")
                        .description("API for managing users"));
    }
}
