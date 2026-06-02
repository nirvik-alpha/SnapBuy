package com.snapBuy.project.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Inject frontend URL from application.properties
    @Value("${frontend.url}")
    String frontEndUrl;

    // Configure Cross-Origin Resource Sharing (CORS) settings
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        /*
        * Apply CORS configuration to all API endpoints
        * Allow specified HTTP methods
        * Allow all request headers
        * Allow cookies, authorization headers, and credentials
         */

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", frontEndUrl)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}