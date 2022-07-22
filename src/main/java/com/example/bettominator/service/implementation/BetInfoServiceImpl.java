package com.example.bettominator.service.implementation;

import com.example.bettominator.model.BetInfo;
import com.example.bettominator.model.Bilten;
import com.example.bettominator.model.SportsGame;
import com.example.bettominator.repostiroy.BetInfoRepository;
import com.example.bettominator.service.BetInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BetInfoServiceImpl implements BetInfoService {

    private final BetInfoRepository betInfoRepository;

    public BetInfoServiceImpl(BetInfoRepository betInfoRepository) {
        this.betInfoRepository = betInfoRepository;
    }

    @Override
    public Optional<BetInfo> findById(Long id) {
        return this.betInfoRepository.findById(id);
    }

    @Override
    public List<BetInfo> findByGameId(Long gameId) {
        return this.betInfoRepository.findBetInfoBySportsGameId(gameId);
    }

    @Override
    public Optional<BetInfo> findByGameIdAndBetType(Long gameId, String betType) {
        return Optional.of(this.betInfoRepository.findBetInfoBySportsGameAndBetType(gameId, betType));
    }

    @Override
    public List<BetInfo> findAll() {
        return this.betInfoRepository.findAll();
    }

    @Override
    public BetInfo save(BetInfo betInfo) {
        return this.betInfoRepository.save(betInfo);
    }

    @Override
    public void deleteById(Long id) {
        this.betInfoRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<BetInfo> betInfoList) {
        if (betInfoList != null) this.betInfoRepository.deleteAllInBatch(betInfoList);
    }

    @Override
    public BetInfo setBetInfo(Bilten bilten, String betTypee, SportsGame sportsGame, Character last) {
        BetInfo betInfo = new BetInfo();
        betInfo.setBetType(betTypee);
        betInfo.setSportsGame(bilten.getSportsGame());
        betInfo.setSelection(last);
        if (last.equals('1')) {
            betInfo.setCoefficient(bilten.getOneCoef());
        } else if (last.equals('2')) {
            betInfo.setCoefficient(bilten.getTwoCoef());
        } else if (last.equals('X')) {
            betInfo.setCoefficient(bilten.getDrawCoef());
        } else if (last.equals('O')) {
            betInfo.setCoefficient(bilten.getOddCoef());
        } else {
            betInfo.setCoefficient(bilten.getEvenCoef());
        }
        return betInfo;
    }
}
