package com.santander.interviewtest.triviagame.controller.dto;

public enum TriviaReplyResultType {

    SUCCESS("right!"),
    BAD_REQUEST("wrong!"),
    FORBIDDEN("Max attempts reached!"),
    NOT_FOUND("No such question!");

    private final String result;

    TriviaReplyResultType(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
