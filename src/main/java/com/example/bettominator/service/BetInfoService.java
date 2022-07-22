package com.example.bettominator.service;

import com.example.bettominator.model.BetInfo;
import com.example.bettominator.model.Bilten;
import com.example.bettominator.model.SportsGame;

import java.util.List;
import java.util.Optional;

public interface BetInfoService {
    Optional<BetInfo> findById(Long id);

    List<BetInfo> findByGameId(Long gameId);

    Optional<BetInfo> findByGameIdAndBetType(Long gameId, String betType);

    List<BetInfo> findAll();

    BetInfo save(BetInfo betInfo);

    void deleteById(Long id);

    void deleteAll(List<BetInfo> betInfoList);

    BetInfo setBetInfo(Bilten bilten, String betTypee, SportsGame sportsGame, Character last);
}
