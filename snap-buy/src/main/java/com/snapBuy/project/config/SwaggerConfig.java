package com.snapBuy.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // Define JWT Bearer authentication scheme for Swagger UI

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)  // HTTP authentication type
                .scheme("bearer")  // Uses Bearer token scheme
                .bearerFormat("JWT")  // Specifies token format as JWT
                .description("JWT Bearer Token");

        // Apply security requirement globally to all APIs

        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");


        // API basic information shown in Swagger UI

        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot eCommerce API")
                        .version("1.0")
                        .description("This is a Spring Boot Project for eCommerce snap-buy")
                        .license(new License().name("Our License Link").url("http://snapbuy.com"))
                        .contact(new Contact()
                                .name("Sadid Rafan")
                                .email("sadid@gmail.com")
                                .url("https://github.com/nirvik-alpha")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("http://snapbuy.com"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerScheme))
                .addSecurityItem(bearerRequirement);
    }


}
