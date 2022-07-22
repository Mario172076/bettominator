package com.example.bettominator.repostiroy;

import com.example.bettominator.model.BetLogs;
import com.example.bettominator.model.ShoppingCart;
import com.example.bettominator.model.SportsGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetLogsRepository extends JpaRepository<BetLogs, Long> {

    List<BetLogs> findBetLogsByCode(String code);

    List<BetLogs> findBetLogsBySportsGame(SportsGame sportsGame);

    List<BetLogs> findBetLogsByShoppingCart(ShoppingCart shoppingCart);
}
