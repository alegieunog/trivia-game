package com.santander.interviewtest.triviagame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "Trivia", uniqueConstraints = @UniqueConstraint(columnNames = {"triviaId"}))
public class Trivia implements Serializable {

    @Serial
    private static final long serialVersionUID = 11111L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "triviaId")
    private Long triviaId;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(name = "answerAttempts", nullable = false)
    private Integer answerAttempts;

    public Long getTriviaId() {
        return triviaId;
    }

    public Trivia setTriviaId(Long triviaId) {
        this.triviaId = triviaId;
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public Trivia setQuestion(String question) {
        this.question = question;
        return this;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public Trivia setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public Integer getAnswerAttempts() {
        return answerAttempts;
    }

    public Trivia setAnswerAttempts(Integer answerAttempts) {
        this.answerAttempts = answerAttempts;
        return this;
    }
}
