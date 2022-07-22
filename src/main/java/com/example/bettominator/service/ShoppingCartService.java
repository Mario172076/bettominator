package com.example.bettominator.service;

import com.example.bettominator.dto.ChargeRequest;
import com.example.bettominator.model.BetInfo;
import com.example.bettominator.model.ShoppingCart;


import java.util.List;

public interface ShoppingCartService {
    List<BetInfo> listAllGamesInShoppingCart(Long cartId);

    ShoppingCart getActiveShoppingCart(String username);

    void deleteAll(List<ShoppingCart> shoppingCarts);

    String checkIfUserHasWon(Long id);

    Boolean checkIfPayoutCompleted(Long id);

    ShoppingCart addGameToShoppingCart(String username, Long gameId, String betType);

    ShoppingCart removeGameFromShoppingCart(String userId, Long gameId);

    ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest);

    ShoppingCart cancelActiveShoppingCart(String userId);

    ShoppingCart createNewShoppingCart(String userId);

    List<ShoppingCart> findAllByUsername(String userId);

    double getCoeffitients(ShoppingCart shoppingCart);

    List<Double> getDoubleCoefficients(ShoppingCart shoppingCart);

    List<ShoppingCart> findAll();

    List<ShoppingCart> findByTicketNumber(String ticketNumber);



    boolean payout(Long id);
}
