package com.santander.interviewtest.triviagame.dto;

import java.util.List;

public record TriviaStartResponse(Integer triviaId,
                                  String question,
                                  List<String> possibleAnswers) {
}
