package com.santander.interviewtest.triviagame.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record TriviaPublicApiJson(@JsonProperty("response_code") Integer responseCode,
                                  @JsonProperty("results") List<TriviaPublicApiResultsJson> results) {
}
