package com.example.bettominator.model;

import com.example.bettominator.model.enumerations.ShoppingCartStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCrated;

    private Double amountOfBet;

    private Boolean payoutCompleted;

    private Double amountToWin;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<BetInfo> betInfoList;

    private ShoppingCartStatus status;

    private String ticketCode;

    public ShoppingCart() {
    }

    public ShoppingCart(User user) {
        this.user = user;
        this.dateCrated = LocalDateTime.now();
        this.betInfoList = new ArrayList<>();
        this.status = ShoppingCartStatus.CREATED;
        this.payoutCompleted=false;
    }

    public ShoppingCart(User user, String ticketCode) {
        this.dateCrated = LocalDateTime.now();
        this.user = user;
        this.betInfoList = new ArrayList<>();
        this.status = ShoppingCartStatus.CREATED;
        this.ticketCode=ticketCode;
        this.payoutCompleted=false;
    }

    public ShoppingCart(Double amountOfBet, Double amountToWin, User user, String ticketCode) {
        this.dateCrated = LocalDateTime.now();
        this.amountOfBet = amountOfBet;
        this.amountToWin = amountToWin;
        this.user = user;
        this.betInfoList = new ArrayList<>();
        this.status = ShoppingCartStatus.CREATED;
        this.ticketCode = ticketCode;
        this.payoutCompleted=false;
    }

}
