package com.santander.interviewtest.triviagame.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record TriviaReplyRequestJson(String answer) {
}
