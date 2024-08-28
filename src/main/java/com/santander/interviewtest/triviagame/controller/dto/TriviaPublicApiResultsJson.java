package com.santander.interviewtest.triviagame.controller.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record TriviaPublicApiResultsJson(@JsonProperty("category") String category,
                                         @JsonProperty("type") String type,
                                         @JsonProperty("difficulty") String difficulty,
                                         @JsonProperty("question") String question,
                                         @JsonProperty("correct_answer") String correctAnswer,
                                         @JsonProperty("incorrect_answers") List<String> incorrectAnswers) {
}
