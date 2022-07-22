package com.example.bettominator.service;


import com.example.bettominator.model.Bilten;

import java.util.List;
import java.util.Optional;

public interface BiltenService {

    Optional<Bilten> findById(Long id);

    List<Bilten> findByGameId(Long gameId);

    Optional<Bilten> findByGameIdAndBetType(Long gameId, String betType);

    List<Bilten> findAll();

    Bilten saveInDataBase(Bilten b);

    void deleteById(Long gameId);

    void deleteAll(List<Bilten> biltenList);
}
