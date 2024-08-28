package com.santander.interviewtest.triviagame.repository;

import com.santander.interviewtest.triviagame.model.Trivia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriviaGameRepository extends JpaRepository<Trivia, Long> {
}
