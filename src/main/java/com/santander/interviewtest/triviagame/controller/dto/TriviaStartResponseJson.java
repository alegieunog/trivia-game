package com.santander.interviewtest.triviagame.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santander.interviewtest.triviagame.dto.TriviaStartResponse;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record TriviaStartResponseJson(Integer triviaId,
                                      String question,
                                      List<String> possibleAnswers) {

    public static TriviaStartResponseJson json(TriviaStartResponse triviaStartResponse) {
        return new TriviaStartResponseJson(triviaStartResponse.triviaId(),
                triviaStartResponse.question(), triviaStartResponse.possibleAnswers());
    }
}
