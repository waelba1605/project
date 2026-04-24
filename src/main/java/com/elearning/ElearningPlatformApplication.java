package com.elearning;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Application class for E-Learning Platform
 * Spring Boot 3.2.0 with MySQL 8.0 and JPA/Hibernate
 */
@SpringBootApplication
public class ElearningPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElearningPlatformApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("E-Learning Platform started successfully!");
        System.out.println("API Documentation: http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================\n");
    }

    /**
     * ModelMapper Bean for DTO to Entity mapping
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
