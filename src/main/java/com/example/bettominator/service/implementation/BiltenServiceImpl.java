package com.example.bettominator.service.implementation;

import com.example.bettominator.model.Bilten;
import com.example.bettominator.repostiroy.BiltenRepository;
import com.example.bettominator.service.BiltenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BiltenServiceImpl implements BiltenService {

    private final BiltenRepository biltenRepository;

    public BiltenServiceImpl(BiltenRepository biltenRepository) {
        this.biltenRepository = biltenRepository;
    }

    @Override
    public Optional<Bilten> findById(Long id) {
        return this.biltenRepository.findById(id);
    }

    @Override
    public List<Bilten> findByGameId(Long gameId) {
        return this.biltenRepository.findBiltenBySportsGameId(gameId);
    }


    @Override
    public Optional<Bilten> findByGameIdAndBetType(Long gameId, String betType) {
        return Optional.of(this.biltenRepository.findBiltenBySportsGameIdAndBetType(gameId, betType));
    }

    @Override
    public List<Bilten> findAll() {
        return this.biltenRepository.findAll();
    }

    @Override
    public Bilten saveInDataBase(Bilten b) {
        return this.biltenRepository.save(b);
    }

    @Override
    public void deleteById(Long gameId) {
        this.biltenRepository.deleteById(gameId);
    }

    @Override
    public void deleteAll(List<Bilten> biltenList) {
        if (biltenList != null) this.biltenRepository.deleteAllInBatch(biltenList);
    }
}
