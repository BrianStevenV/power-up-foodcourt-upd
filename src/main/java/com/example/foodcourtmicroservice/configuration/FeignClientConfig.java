package com.example.foodcourtmicroservice.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Value("${jwt.secret}")
    private String jwtToken;

    @Bean
    System.Logger.Level feignLoggerLevel(){
        return System.Logger.Level.INFO;
    }

    @Bean
    public RequestInterceptor jwtInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + jwtToken);
        };
    }
}
