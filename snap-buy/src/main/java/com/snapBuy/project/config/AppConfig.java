package com.snapBuy.project.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Application configuration class.
 * Defines and manages application-wide beans
 * used across the Spring Boot application.
 */

@Configuration
public class AppConfig {


    /**
     * Provides a ModelMapper bean.
     * ModelMapper is used for object mapping between
     * Entity and DTO classes, reducing boilerplate code.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
