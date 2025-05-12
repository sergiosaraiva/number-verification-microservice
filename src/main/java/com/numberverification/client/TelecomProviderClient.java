package com.numberverification.client;

import com.numberverification.config.TelecomProviderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TelecomProviderClient {
    private static final Logger log = LoggerFactory.getLogger(TelecomProviderClient.class);
    
    private final WebClient webClient;
    private final TelecomProviderProperties properties;
    
    public TelecomProviderClient(WebClient.Builder webClientBuilder, TelecomProviderProperties properties) {
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                .build();
        this.properties = properties;
    }
    
    public Mono<Boolean> verifyPhoneNumberMatch(String phoneNumber) {
        log.debug("Verifying phone number match: {}", phoneNumber);
        
        // In a real implementation, this would call the telecom provider API
        // For demo purposes, we're returning a mock response
        return Mono.just(true);
        
        /* Real implementation would look like this:
        return webClient.post()
                .uri("/verify")
                .bodyValue(Map.of("msisdn", phoneNumber))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Boolean match = (Boolean) response.get("match");
                    return match != null && match;
                })
                .onErrorResume(e -> {
                    log.error("Error verifying phone number: {}", e.getMessage());
                    return Mono.just(false);
                });
        */
    }
    
    public Mono<String> getDevicePhoneNumber() {
        log.debug("Retrieving device phone number");
        
        // Mock implementation for demo
        return Mono.just("+34698765432");
        
        /* Real implementation would look like this:
        return webClient.get()
                .uri("/device-phone-number")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("phoneNumber"))
                .onErrorResume(e -> {
                    log.error("Error retrieving device phone number: {}", e.getMessage());
                    return Mono.empty();
                });
        */
    }
}