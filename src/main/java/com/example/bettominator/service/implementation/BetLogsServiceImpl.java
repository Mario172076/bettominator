package com.example.bettominator.service.implementation;

import com.example.bettominator.model.BetLogs;
import com.example.bettominator.model.ShoppingCart;
import com.example.bettominator.model.SportsGame;
import com.example.bettominator.repostiroy.BetLogsRepository;
import com.example.bettominator.service.BetLogsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetLogsServiceImpl implements BetLogsService {

    private final BetLogsRepository betLogsRepository;

    public BetLogsServiceImpl(BetLogsRepository betLogsRepository) {
        this.betLogsRepository = betLogsRepository;
    }

    @Override
    public List<BetLogs> findByCode(String code) {
        return this.betLogsRepository.findBetLogsByCode(code);
    }

    @Override
    public List<BetLogs> findByGameId(SportsGame sportsGame) {
        return this.betLogsRepository.findBetLogsBySportsGame(sportsGame);
    }

    @Override
    public List<BetLogs> findByShoppingCart(ShoppingCart shoppingCart) {
        return this.betLogsRepository.findBetLogsByShoppingCart(shoppingCart);
    }

    @Override
    public List<BetLogs> findAll() {
        return this.betLogsRepository.findAll();
    }

    @Override
    public BetLogs save(BetLogs betLogs) {
        return this.betLogsRepository.save(betLogs);
    }


    @Override
    public void deleteAll(List<BetLogs> betLogs) {
        this.betLogsRepository.deleteAllInBatch(betLogs);
    }


}
