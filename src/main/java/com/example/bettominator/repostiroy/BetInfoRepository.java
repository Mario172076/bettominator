package com.example.bettominator.repostiroy;

import com.example.bettominator.model.BetInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BetInfoRepository extends JpaRepository<BetInfo, Long> {

    BetInfo findBetInfoBySportsGameAndBetType(Long gameId, String betType);

    List<BetInfo> findBetInfoBySportsGameId(Long gameId);

}
