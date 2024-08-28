package com.santander.interviewtest.triviagame.service;

import com.santander.interviewtest.triviagame.controller.dto.TriviaPublicApiJson;
import com.santander.interviewtest.triviagame.controller.dto.TriviaPublicApiResultsJson;
import com.santander.interviewtest.triviagame.dto.TriviaStartResponse;
import com.santander.interviewtest.triviagame.model.Trivia;
import com.santander.interviewtest.triviagame.repository.TriviaGameRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.BAD_REQUEST;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.FORBIDDEN;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.NOT_FOUND;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.SUCCESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TriviaGameServiceTest {

    private final TriviaPublicApiService triviaPublicApiService = mock(TriviaPublicApiService.class);
    private final TriviaGameRepository triviaGameRepository = mock(TriviaGameRepository.class);

    private final TriviaGameService underTest = new TriviaGameService(triviaPublicApiService, triviaGameRepository);

    @Test
    void startTrivia_whenGetRandomTriviaQuestionCalled_thenTriviaStartResponse() {
        final var triviaId = 1L;
        final var question = "Which soccer team won the Copa America 2015 Championship ?";
        final var correctAnswer = "Chile";
        final var incorrectAnswers = List.of("Argentina", "Brazil", "Paraguay");
        final var possibleAnswers = List.of(correctAnswer, "Argentina", "Brazil", "Paraguay");
        final var trivia = new Trivia()
                .setTriviaId(triviaId)
                .setQuestion(question)
                .setCorrectAnswer(correctAnswer)
                .setAnswerAttempts(0);

        final var expectedResponse
                = new TriviaStartResponse(1, question, possibleAnswers);
        final var apiResults
                = new TriviaPublicApiResultsJson("Sports", "multiple", "medium", question, correctAnswer, incorrectAnswers);
        final var triviaApi = new TriviaPublicApiJson(0, List.of(apiResults));

        when(triviaPublicApiService.getRandomTriviaQuestion()).thenReturn(triviaApi);
        when(triviaGameRepository.save(any())).thenReturn(trivia);

        var actualResponse = underTest.startTrivia();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void replyTrivia_whenAnswerIsCorrect_thenReturnSuccess() {
        final var triviaId = 1L;
        final var question = "Which soccer team won the Copa America 2015 Championship ?";
        final var correctAnswer = "Chile";

        final var triviaDbResponse = new Trivia()
                .setTriviaId(triviaId)
                .setQuestion(question)
                .setCorrectAnswer(correctAnswer)
                .setAnswerAttempts(0);

        when(triviaGameRepository.findById(triviaId)).thenReturn(Optional.of(triviaDbResponse));

        final var actualResponse = underTest.replyTrivia(String.valueOf(triviaId), correctAnswer);
        assertThat(actualResponse.result()).isEqualTo(SUCCESS.getResult());
    }

    @Test
    void replyTrivia_whenAnswerIsWrong_thenReturnBadRequest() {
        final var triviaId = 1L;
        final var answer = "Argentina";
        final var question = "Which soccer team won the Copa America 2015 Championship ?";
        final var correctAnswer = "Chile";

        final var triviaDbResponse = new Trivia()
                .setTriviaId(triviaId)
                .setQuestion(question)
                .setCorrectAnswer(correctAnswer)
                .setAnswerAttempts(0);

        when(triviaGameRepository.findById(triviaId)).thenReturn(Optional.of(triviaDbResponse));

        final var actualResponse = underTest.replyTrivia(String.valueOf(triviaId), answer);
        assertThat(actualResponse.result()).isEqualTo(BAD_REQUEST.getResult());
    }

    @Test
    void replyTrivia_whenMaxAttemptsReached_thenReturnForbidden() {
        final var triviaId = 1L;
        final var question = "Which soccer team won the Copa America 2015 Championship ?";
        final var correctAnswer = "Chile";

        final var triviaDbResponse = new Trivia()
                .setTriviaId(triviaId)
                .setQuestion(question)
                .setCorrectAnswer(correctAnswer)
                .setAnswerAttempts(3);

        when(triviaGameRepository.findById(triviaId)).thenReturn(Optional.of(triviaDbResponse));

        final var actualResponse = underTest.replyTrivia(String.valueOf(triviaId), correctAnswer);
        assertThat(actualResponse.result()).isEqualTo(FORBIDDEN.getResult());
    }

    @Test
    void replyTrivia_whenRecordNotFound_thenReturnNotFound() {
        final var triviaId = 10L;
        final var correctAnswer = "Chile";

        when(triviaGameRepository.findById(triviaId)).thenReturn(Optional.empty());

        final var actualResponse = underTest.replyTrivia(String.valueOf(triviaId), correctAnswer);
        assertThat(actualResponse.result()).isEqualTo(NOT_FOUND.getResult());
    }
}