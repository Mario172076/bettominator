package com.example.bettominator.service;

import com.example.bettominator.model.Category;
import com.example.bettominator.model.Country;
import com.example.bettominator.model.SportsGame;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GameService {

    List<SportsGame> findAll();

    Optional<SportsGame> findById(Long id);

    SportsGame save(SportsGame sportsGame) throws IOException;

    SportsGame saveInDataBase(SportsGame sportsGame) throws IOException;

    Optional<SportsGame> edit(Long id, SportsGame product) throws IOException;

    List<SportsGame> hasNotStarted();

    void deleteById(Long id);

    void deleteAll(List<SportsGame> gameSet);

    void deleteAllThatFinished();

    List<SportsGame> listGamesByCompetitorsAndCategoryAndCountry(String competitorName, Category category, Country country);

    List<SportsGame> listGamesByDate(String lastMinute, String today);

    List<SportsGame> fullSearch(String playingLive, String today, String betnow, Category category, Country country, String competitorName);


}
