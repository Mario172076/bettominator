package com.example.bettominator.repostiroy;


import com.example.bettominator.model.Bilten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiltenRepository extends JpaRepository<Bilten, Long> {
    Bilten findBiltenBySportsGameIdAndBetType(Long gameId, String betType);
    List<Bilten> findBiltenBySportsGameId(Long gameId);
}
