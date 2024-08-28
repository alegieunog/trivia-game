package com.santander.interviewtest.triviagame.controller;

import com.santander.interviewtest.triviagame.controller.dto.TriviaReplyRequestJson;
import com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResponseJson;
import com.santander.interviewtest.triviagame.controller.dto.TriviaStartResponseJson;
import com.santander.interviewtest.triviagame.service.TriviaGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResponseJson.json;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaReplyResultType.*;
import static com.santander.interviewtest.triviagame.controller.dto.TriviaStartResponseJson.json;

@RestController
@RequestMapping(value = "/trivia")
public record TriviaGameController(TriviaGameService triviaGameService) {

    @PostMapping(value = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TriviaStartResponseJson> startTrivia() {
        var startResponse = triviaGameService.startTrivia();
        return ResponseEntity.ok(json(startResponse));
    }

    @PutMapping(value = "reply/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TriviaReplyResponseJson> replyTrivia(@PathVariable String id,
                                                               @RequestBody TriviaReplyRequestJson triviaReplyRequest) {
        var replyResponse = triviaGameService.replyTrivia(id, triviaReplyRequest.answer());

        if (replyResponse.result().equals(BAD_REQUEST.getResult())) {
            return ResponseEntity.badRequest().body(json(replyResponse));
        }
        if (replyResponse.result().equals(FORBIDDEN.getResult())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(json(replyResponse));
        }
        if (replyResponse.result().equals(NOT_FOUND.getResult())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(json(replyResponse));
        }

        return ResponseEntity.ok(json(replyResponse));
    }
}
