package com.example.bettominator.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import java.util.List;

@Data
@Entity
public class BetInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String betType;

    @ManyToOne
    private SportsGame sportsGame;

    @JsonIgnore
    @ManyToMany(mappedBy = "betInfoList")
    private List<ShoppingCart> shoppingCarts;

    private Character selection;

    private double coefficient;


    public BetInfo(String betType, SportsGame sportsGame, Character selection, double coefficient) {
        this.betType = betType;
        this.sportsGame = sportsGame;
        this.selection = selection;
        this.coefficient = coefficient;
    }

    public BetInfo() {

    }


}
