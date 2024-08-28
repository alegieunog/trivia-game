package com.santander.interviewtest.triviagame.controller;

import com.santander.interviewtest.triviagame.dto.TriviaReplyResponse;
import com.santander.interviewtest.triviagame.dto.TriviaStartResponse;
import com.santander.interviewtest.triviagame.service.TriviaGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.BAD_REQUEST;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.FORBIDDEN;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.NOT_FOUND;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.SUCCESS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {TriviaGameController.class})
public class TriviaGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TriviaGameService triviaGameService;

    @Test
    void postMapping_whenStartCalled_thenReturnTriviaStartResponseJson() throws Exception {
        final var question = "Which soccer team won the Copa America 2015 Championship ?";
        final var possibleAnswers = List.of("Chile", "Argentina", "Brazil", "Paraguay");
        final var triviaStartResponse
                = new TriviaStartResponse(1, question, possibleAnswers);

        when(triviaGameService.startTrivia()).thenReturn(triviaStartResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/trivia/start")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.triviaId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("Which soccer team won the Copa America 2015 Championship ?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.possibleAnswers").isArray());
    }

    @Test
    void putMapping_givenRightAnswer_whenReplyCalled_thenReturnOk() throws Exception {
        final String triviaReplyRequest = "{\"answer\": \"Chile\"}";
        final var triviaReplyResponse = new TriviaReplyResponse(SUCCESS.getResult());

        when(triviaGameService.replyTrivia(anyString(), anyString())).thenReturn(triviaReplyResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/trivia/reply/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(triviaReplyRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("right!"));
    }

    @Test
    void putMapping_givenWrongAnswer_whenReplyCalled_thenReturnBadRequest() throws Exception {
        final String triviaReplyRequest = "{\"answer\": \"Brazil\"}";
        final var triviaReplyResponse = new TriviaReplyResponse(BAD_REQUEST.getResult());

        when(triviaGameService.replyTrivia(anyString(), anyString())).thenReturn(triviaReplyResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/trivia/reply/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(triviaReplyRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("wrong!"));
    }

    @Test
    void putMapping_givenMaxAttemptsReached_whenReplyCalled_thenReturnForbidden() throws Exception {
        final String triviaReplyRequest = "{\"answer\": \"Chile\"}";
        final var triviaReplyResponse = new TriviaReplyResponse(FORBIDDEN.getResult());

        when(triviaGameService.replyTrivia(anyString(), anyString())).thenReturn(triviaReplyResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/trivia/reply/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(triviaReplyRequest))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Max attempts reached!"));
    }

    @Test
    void putMapping_givenDbRecordNotFound_whenReplyCalled_thenReturnNotFound() throws Exception {
        final String triviaReplyRequest = "{\"answer\": \"Chile\"}";
        final var triviaReplyResponse = new TriviaReplyResponse(NOT_FOUND.getResult());

        when(triviaGameService.replyTrivia(anyString(), anyString())).thenReturn(triviaReplyResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/trivia/reply/10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(triviaReplyRequest))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("No such question!"));
    }
}