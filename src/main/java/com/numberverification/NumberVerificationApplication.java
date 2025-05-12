package com.numberverification;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Number Verification API",
        version = "1.0",
        description = "CAMARA API for Number Verification"
    )
)
public class NumberVerificationApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NumberVerificationApplication.class, args);
    }
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}