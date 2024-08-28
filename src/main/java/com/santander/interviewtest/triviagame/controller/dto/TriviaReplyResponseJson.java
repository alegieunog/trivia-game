package com.santander.interviewtest.triviagame.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.santander.interviewtest.triviagame.dto.TriviaReplyResponse;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record TriviaReplyResponseJson(String result) {

    public static TriviaReplyResponseJson json(TriviaReplyResponse triviaReplyResponse) {
        return new TriviaReplyResponseJson(triviaReplyResponse.result());
    }
}
