package com.example.bettominator.service;

import com.example.bettominator.model.BetLogs;
import com.example.bettominator.model.ShoppingCart;
import com.example.bettominator.model.SportsGame;

import java.util.List;

public interface BetLogsService {


    List<BetLogs> findByCode(String code);

    List<BetLogs> findByGameId(SportsGame sportsGame);

    List<BetLogs> findByShoppingCart(ShoppingCart shoppingCart);

    List<BetLogs> findAll();

    BetLogs save(BetLogs betLogs);

    void deleteAll(List<BetLogs> betLogs);


}
