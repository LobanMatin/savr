package com.lobanmatin.budget_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Savr Budgeting API")
                        .version("0.1.0")
                        .description("Initial development version of the Savr budgeting API.")
                        .contact(new Contact()
                                .name("Loban Matin")
                                .email("loban.matin@gmail.com")
                        ));
    }
}
