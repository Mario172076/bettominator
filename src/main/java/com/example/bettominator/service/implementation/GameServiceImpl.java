package com.example.bettominator.service.implementation;

import com.example.bettominator.model.*;
import com.example.bettominator.model.exceptions.CategoryNotFoundException;
import com.example.bettominator.model.exceptions.CountryNotFoundException;
import com.example.bettominator.model.exceptions.GameNotFoundException;
import com.example.bettominator.repostiroy.GameRepository;
import com.example.bettominator.service.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final CategoryService categoryService;
    private final CountryService countryService;
    private final BiltenService biltenService;
    private final BetInfoService betInfoService;
    private final BetLogsService betLogsService;

    public GameServiceImpl(GameRepository gameRepository, CategoryService categoryService, CountryService countryService, BiltenService biltenService, BetInfoService betInfoService, BetLogsService betLogsService) {
        this.gameRepository = gameRepository;
        this.categoryService = categoryService;
        this.countryService = countryService;

        this.biltenService = biltenService;
        this.betInfoService = betInfoService;
        this.betLogsService = betLogsService;
    }

    @Override
    public List<SportsGame> findAll() {
        return this.gameRepository.findAll();
    }

    @Override
    public Optional<SportsGame> findById(Long id) {
        return this.gameRepository.findById(id);
    }

    @Override
    public SportsGame save(SportsGame sportsGame) throws IOException {
        Category category = this.categoryService.findById(sportsGame.getCategory().getId()).
                orElseThrow(() -> new CategoryNotFoundException(sportsGame.getCategory().getId()));
        Country country = this.countryService.findById(sportsGame.getCountry().getId()).orElseThrow(() -> new CountryNotFoundException(sportsGame.getCountry().getId()));
        sportsGame.setCategory(category);
        sportsGame.setCountry(country);
        return this.gameRepository.save(sportsGame);
    }

    @Override
    public SportsGame saveInDataBase(SportsGame sportsGame) throws IOException {
        return this.gameRepository.save(sportsGame);
    }

    @Override
    public Optional<SportsGame> edit(Long id, SportsGame sGame) throws IOException {
        SportsGame game = this.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        Category category = this.categoryService.findById(game.getCategory().getId()).orElseThrow(() -> new CategoryNotFoundException(sGame.getCategory().getId()));
        Country country = this.countryService.findById(game.getCountry().getId()).orElseThrow(() -> new CountryNotFoundException(sGame.getCountry().getId()));
        game.setCategory(category);
        game.setCountry(country);
        game.setCompetitorOnePoints(sGame.getCompetitorOnePoints());
        game.setCompetitorTwoPoints(sGame.getCompetitorTwoPoints());
        game.setCompetitorOne(sGame.getCompetitorOne());
        game.setCompetitorTwo(sGame.getCompetitorTwo());
        return Optional.of(this.gameRepository.save(game));
    }

    @Override
    public List<SportsGame> hasNotStarted() {
        return this.gameRepository.findAll().stream().filter(SportsGame::playingToday).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.gameRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<SportsGame> gameSet) {
        this.gameRepository.deleteAllInBatch(gameSet);
    }

    @Override
    public void deleteAllThatFinished() {
        List<SportsGame> games = this.findAll();
        for(SportsGame sportsGame: games){
            if(!sportsGame.isFinishedToday() && !sportsGame.isLive() && !sportsGame.playingToday()){
                List<BetLogs> bL = this.betLogsService.findByGameId(sportsGame);
                this.betLogsService.deleteAll(bL);
                List<Bilten> b = this.biltenService.findByGameId(sportsGame.getId());
                this.biltenService.deleteAll(b);
                List<BetInfo> bf = this.betInfoService.findByGameId(sportsGame.getId());
                this.betInfoService.deleteAll(bf);
                this.deleteById(sportsGame.getId());
            }
        }
        games = games.stream().filter(i-> i.isFinishedToday() || i.isLive()).collect(Collectors.toList());
        for(SportsGame sportsGame: games){
            List<Bilten> b = this.biltenService.findByGameId(sportsGame.getId());
            this.biltenService.deleteAll(b);
            List<BetInfo> bf = this.betInfoService.findByGameId(sportsGame.getId());
            this.betInfoService.deleteAll(bf);
        }
    }

    @Override
    public List<SportsGame> listGamesByDate(String playingToday, String today) {
        List<SportsGame> sportsGames = this.gameRepository.findAll();
        if (playingToday != null && today == null) {
            sportsGames.removeIf(sp -> !sp.isLive());
            return sportsGames;
        } else if (today != null && playingToday == null) {
            sportsGames.removeIf(sp -> !sp.isFinishedToday());
            return sportsGames;
        } else {
            sportsGames = new ArrayList<>();
            return sportsGames;
        }
    }

    @Override
    public List<SportsGame> listGamesByCompetitorsAndCategoryAndCountry(String competitor, Category category, Country country) {
        String competitorLike = competitor != null ? "%" + competitor + "%" : null;
        if (category != null && country != null)
            return this.gameRepository.findAllByCategoryAndCountry(category, country);
        else if (category != null) return this.gameRepository.findAllByCategory(category);
        else if (country != null) return this.gameRepository.findAllByCountry(country);
        else if (competitor != null) {
            List<SportsGame> sportsGames = new ArrayList<>();
            sportsGames.addAll(this.gameRepository.findAllByCompetitorOneLikeIgnoreCase(competitorLike));
            sportsGames.addAll(this.gameRepository.findAllByCompetitorTwoLikeIgnoreCase(competitorLike));
            if (sportsGames.size() != 0) {
                return sportsGames;
            } else {
                return this.gameRepository.findAll();
            }
        }
        else return this.gameRepository.findAll();
    }

    @Override
    public List<SportsGame> fullSearch(String playingLive, String today, String betnow, Category category, Country country, String competitorName) {
        List<SportsGame> listGames;
        if (playingLive != null || today != null) {
            listGames = this.listGamesByDate(playingLive, today);
        } else if (betnow != null) {
            listGames = this.hasNotStarted();
        } else if (category != null || country != null || competitorName != null) {
            listGames = this.listGamesByCompetitorsAndCategoryAndCountry(competitorName, category, country)
                    .stream().filter(SportsGame::playingToday).collect(Collectors.toList());
        } else {
            listGames = this.findAll().stream().filter(SportsGame::playingToday).collect(Collectors.toList());
        }
        return listGames;
    }


}
