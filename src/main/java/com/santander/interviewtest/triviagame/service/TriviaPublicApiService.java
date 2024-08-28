package com.santander.interviewtest.triviagame.service;

import com.santander.interviewtest.triviagame.controller.dto.TriviaPublicApiJson;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public record TriviaPublicApiService(WebClient webClient) {

    public TriviaPublicApiJson getRandomTriviaQuestion() {
        return webClient.get()
                .retrieve()
                .bodyToMono(TriviaPublicApiJson.class)
                .block();
    }

}
