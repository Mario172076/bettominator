package com.example.bettominator.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;

@Data
@Entity
public class SportsGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String competitorOne;

    private String competitorTwo;

    Integer competitorOnePoints;

    Integer competitorTwoPoints;

    Integer competitorOnePointsFH;

    Integer competitorTwoPointsFH;

    LocalDateTime startOfMatch;

    LocalDateTime endOfMatch;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Category category;

    public SportsGame() {
    }

    public SportsGame(long id, String competitorOne, String competitorTwo, Integer competitorOnePoints, Integer competitorTwoPoints, LocalDateTime startOfMatch, LocalDateTime endOfMatch, Category category, Country country) {
        this.id = id;
        this.competitorOne = competitorOne;
        this.competitorTwo = competitorTwo;
        this.competitorOnePoints = competitorOnePoints;
        this.competitorTwoPoints = competitorTwoPoints;
        this.startOfMatch = startOfMatch;
        this.endOfMatch = endOfMatch;
        this.category = category;
        this.country = country;
    }

    public SportsGame(String competitorOne, String competitorTwo, Integer competitorOnePoints, Integer competitorTwoPoints, Integer competitorOnePointsFH, Integer competitorTwoPointsFH, LocalDateTime startOfMatch, LocalDateTime endOfMatch, Country country, Category category) {
        this.competitorOne = competitorOne;
        this.competitorTwo = competitorTwo;
        this.competitorOnePoints = competitorOnePoints;
        this.competitorTwoPoints = competitorTwoPoints;
        this.competitorOnePointsFH = competitorOnePointsFH;
        this.competitorTwoPointsFH = competitorTwoPointsFH;
        this.startOfMatch = startOfMatch;
        this.endOfMatch = endOfMatch;
        this.country = country;
        this.category = category;
    }

    public String getGameInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        if (getEndOfMatch().isBefore(LocalDateTime.now())) {
            String formatDateTime = getEndOfMatch().format(formatter);
            sb.append(String.format("%s : %s %d:%d\n", competitorOne, competitorTwo, competitorOnePoints, competitorTwoPoints));
            sb.append(String.format("(%s , %s)\n", country.getName(), category.getName()));
            sb.append(String.format("End: %s", formatDateTime));
        } else if (getStartOfMatch().isAfter(LocalDateTime.now())) {
            String formatDateTime = getStartOfMatch().format(formatter);
            sb.append(String.format("%s : %s\n", competitorOne, competitorTwo));
            sb.append(String.format("(%s , %s)\n", country.getName(), category.getName()));
            sb.append(String.format("Start: %s", formatDateTime));
        } else {
            sb.append(String.format("%s : %s %d:%d\n", competitorOne, competitorTwo, competitorOnePointsFH, competitorTwoPointsFH));
            sb.append(String.format("(%s , %s)\n", country.getName(), category.getName()));
            long minutes = ChronoUnit.MINUTES.between(this.startOfMatch, this.endOfMatch);
            if(Long.parseLong(getMinutesLeft()) < (minutes/2)) sb.append("SECOND HALF\n");
            else sb.append("FIRST HALF\n");
            sb.append(String.format("Minutes left: %s", getMinutesLeft()));
        }
        return sb.toString();
    }

    public HashMap<String, Character> getGameResults(){
        HashMap<String, Character> data = new HashMap<>();
            if(competitorOnePoints>competitorTwoPoints){
                data.put("PICKWINNER", '1');
                data.put("SECONDHALF", '1');
            } else if(competitorOnePoints<competitorTwoPoints){
                data.put("PICKWINNER", '2');
                data.put("SECONDHALF", '2');
            }else{
                data.put("PICKWINNER", 'X');
                data.put("SECONDHALF", 'X');
            }

            if((competitorOnePoints+competitorTwoPoints)%2==0){
                data.put("ODDEVEN", 'E');
            }
            else{
                data.put("ODDEVEN", 'O');
            }

            if(competitorOnePointsFH > competitorTwoPointsFH){
                data.put("FIRSTHALF", '1');

            }else if(competitorOnePointsFH < competitorTwoPointsFH){
                data.put("FIRSTHALF", '2');
            }
            else {
                data.put("FIRSTHALF", 'X');
            }

        return data;
    }

    public boolean isFinishedToday() {
        return (this.endOfMatch.isBefore(LocalDateTime.now()) && (this.endOfMatch.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()
        ||   LocalDateTime.now().getDayOfMonth() - this.endOfMatch.getDayOfMonth() <=2 ));
    }

    public boolean playingToday() {
        return(this.startOfMatch.isAfter(LocalDateTime.now()) && this.endOfMatch.isAfter(LocalDateTime.now()) &&
                (this.startOfMatch.getDayOfMonth()==LocalDateTime.now().getDayOfMonth() || this.startOfMatch.getDayOfMonth() - LocalDateTime.now().getDayOfMonth()==1));
    }

    public boolean isLive(){
        return (((this.startOfMatch.getDayOfMonth()==LocalDateTime.now().getDayOfMonth() && this.endOfMatch.getDayOfMonth()==LocalDateTime.now().getDayOfMonth())
        || (LocalDateTime.now().getDayOfMonth() - this.startOfMatch.getDayOfMonth()==1 && this.endOfMatch.getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
        || (LocalDateTime.now().getDayOfMonth() == this.startOfMatch.getDayOfMonth() && this.endOfMatch.getDayOfMonth() - LocalDateTime.now().getDayOfMonth()==1))
        && this.startOfMatch.isBefore(LocalDateTime.now()) && this.endOfMatch.isAfter(LocalDateTime.now()));
    }


    public String getMinutesLeft(){
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), this.endOfMatch);
        return String.valueOf(minutes);
    }
}
