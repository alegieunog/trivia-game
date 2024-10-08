package com.santander.interviewtest.triviagame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class TriviaGameConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://opentdb.com/api.php?amount=1")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}
