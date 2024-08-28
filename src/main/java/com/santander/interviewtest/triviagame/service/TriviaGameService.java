package com.santander.interviewtest.triviagame.service;

import com.santander.interviewtest.triviagame.controller.dto.TriviaPublicApiResultsJson;
import com.santander.interviewtest.triviagame.dto.TriviaReplyResponse;
import com.santander.interviewtest.triviagame.dto.TriviaStartResponse;
import com.santander.interviewtest.triviagame.model.Trivia;
import com.santander.interviewtest.triviagame.repository.TriviaGameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.BAD_REQUEST;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.FORBIDDEN;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.NOT_FOUND;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.SUCCESS;

@Service
public record TriviaGameService(TriviaPublicApiService triviaPublicApiService,
                                TriviaGameRepository triviaGameRepository) {

    public TriviaStartResponse startTrivia() {
        final var triviaQuestion = triviaPublicApiService.getRandomTriviaQuestion();
        final var triviaQuestionResult = triviaQuestion.results()
                .stream()
                .findFirst().get();
        final var triviaDbRecord = triviaGameRepository.save(buildTriviaDbRecord(triviaQuestionResult));
        return buildTriviaStartResponse(triviaDbRecord, triviaQuestionResult.incorrectAnswers());
    }

    public TriviaReplyResponse replyTrivia(String id, String answer) {
        final var triviaDbRecord = triviaGameRepository.findById(Long.valueOf(id));
        if (triviaDbRecord.isEmpty()) {
            return new TriviaReplyResponse(NOT_FOUND.getResult());
        }

        final var triviaQuestion = triviaDbRecord.get();
        if (triviaQuestion.getAnswerAttempts() >= 3) {
            return new TriviaReplyResponse(FORBIDDEN.getResult());
        }

        if (triviaQuestion.getCorrectAnswer().equals(answer)) {
            triviaGameRepository.deleteById(triviaQuestion.getTriviaId());
            return new TriviaReplyResponse(SUCCESS.getResult());
        }

        triviaQuestion.setAnswerAttempts(triviaQuestion.getAnswerAttempts()+1);
        triviaGameRepository.save(triviaQuestion);
        return new TriviaReplyResponse(BAD_REQUEST.getResult());
    }

    private Trivia buildTriviaDbRecord(TriviaPublicApiResultsJson apiResultsJson) {
        return new Trivia().setQuestion(apiResultsJson.question())
                .setCorrectAnswer(apiResultsJson.correctAnswer())
                .setAnswerAttempts(0);
    }

    private TriviaStartResponse buildTriviaStartResponse(Trivia trivia, List<String> incorrectAnswers) {
        List<String> possibleAnswers = new ArrayList<>(incorrectAnswers);
        possibleAnswers.add(trivia.getCorrectAnswer());
        Collections.shuffle(possibleAnswers);
        return new TriviaStartResponse(Math.toIntExact(trivia.getTriviaId()), trivia.getQuestion(), possibleAnswers);
    }
}
