package com.example.bettominator.repostiroy;

import com.example.bettominator.model.ShoppingCart;
import com.example.bettominator.model.User;
import com.example.bettominator.model.enumerations.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserAndStatus(User user, ShoppingCartStatus status);

    Optional<ShoppingCart> findByUserUsernameAndStatus(String username, ShoppingCartStatus status);

    boolean existsByUserUsernameAndStatus(String username, ShoppingCartStatus status);

    List<ShoppingCart> findAllByUserUsername(String username);

    List<ShoppingCart> findShoppingCartByTicketCodeLike(String ticketCode);

}
