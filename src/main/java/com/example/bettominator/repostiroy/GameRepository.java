package com.example.bettominator.repostiroy;

import com.example.bettominator.model.Category;
import com.example.bettominator.model.Country;
import com.example.bettominator.model.SportsGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<SportsGame, Long> {

    List<SportsGame> findAllByCategory(Category category);

    List<SportsGame> findAllByCategoryAndCountry(Category category, Country country);

    List<SportsGame> findAllByCountry(Country country);

    List<SportsGame> findAllByCompetitorOneLikeIgnoreCase(String competitorOne);

    List<SportsGame> findAllByCompetitorTwoLikeIgnoreCase(String competitorOne);


}
