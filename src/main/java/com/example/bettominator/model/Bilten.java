package com.example.bettominator.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Bilten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String betType;
    private Double oneCoef;
    private Double drawCoef;
    private Double twoCoef;
    private Double oddCoef;
    private Double evenCoef;

    @ManyToOne
    private SportsGame sportsGame;

    public Bilten(String betType, Double oneCoef, Double drawCoef, Double twoCoef, Double oddCoef, Double evenCoef, SportsGame sportsGame) {
        this.betType = betType;
        this.oneCoef = oneCoef;
        this.drawCoef = drawCoef;
        this.twoCoef = twoCoef;
        this.oddCoef = oddCoef;
        this.evenCoef = evenCoef;
        this.sportsGame = sportsGame;
    }

    public Bilten() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
