package com.example.bettominator.service;

import com.example.bettominator.model.Bilten;
import com.example.bettominator.model.SportsGame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface FillDataBaseService {
    void FillDataBase() throws IOException;

    List<LocalDateTime> crateDate(int i);

    Set<SportsGame> generateGames() throws IOException;

    List<Bilten> generateBilten(Set<SportsGame> games);
}
