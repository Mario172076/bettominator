package com.example.bettominator.model;


import lombok.Data;
import javax.persistence.*;


@Data
@Entity
public class BetLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    private SportsGame sportsGame;

    @ManyToOne
    private ShoppingCart shoppingCart;

    private String betType;

    private Character selection;

    public BetLogs(String code, SportsGame sportsGame, String betType, Character selection, ShoppingCart shoppingCart) {
        this.code = code;
        this.sportsGame = sportsGame;
        this.betType = betType;
        this.selection = selection;
        this.shoppingCart=shoppingCart;
    }

    public BetLogs() {
    }
}
