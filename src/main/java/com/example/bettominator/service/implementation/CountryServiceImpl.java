package com.example.bettominator.service.implementation;

import com.example.bettominator.model.Country;
import com.example.bettominator.model.SportsGame;
import com.example.bettominator.model.exceptions.CountryNameException;
import com.example.bettominator.model.exceptions.CountryNotFoundException;
import com.example.bettominator.repostiroy.CountryRepository;
import com.example.bettominator.repostiroy.GameRepository;
import com.example.bettominator.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final GameRepository gameRepository;

    public CountryServiceImpl(CountryRepository countryRepository, GameRepository gameRepository) {
        this.countryRepository = countryRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<Country> findById(Long id) {
        return this.countryRepository.findById(id);
    }


    @Override
    public Country findByName(String name) {
        return this.countryRepository.findCountryByName(name);
    }

    @Override
    public List<Country> findCountryByNameLike(String name) {
        String nameLike = name != null ? "%" + name + "%" : null;
        if (nameLike != null) {
            return this.countryRepository.findCountryByNameLikeIgnoreCase(nameLike);
        } else {
            return this.countryRepository.findAll();
        }
    }

    @Override
    public List<Country> findAll() {
        return this.countryRepository.findAll();
    }


    @Override
    public Country create(String name) {
        if (name == null || name.isEmpty()) {
            throw new CountryNameException();
        }
        Country c = new Country(name);
        countryRepository.save(c);
        return c;
    }

    @Override
    public Optional<Country> edit(Long id, String name, MultipartFile image) throws IOException {
        Country c = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException(id));
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            c.setImageBase64(base64Image);
        } else {
            throw new IOException();
        }
        c.setName(name);

        return Optional.of(this.countryRepository.save(c));
    }

    @Override
    public Optional<Country> save(Country country, MultipartFile image) throws IOException {
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            country.setImageBase64(base64Image);
        } else {
            throw new IOException();
        }
        return Optional.of(this.countryRepository.save(country));
    }


    @Override
    public void delete(String name) {
        this.countryRepository.deleteByName(name);
    }


    @Override
    public void deleteById(Long id) {
        this.countryRepository.deleteById(id);
    }

    @Override
    public boolean deleteCountryById(Long id) {
        Optional<Country> con = this.countryRepository.findById(id);
        List<SportsGame> games = this.gameRepository.findAll();
        for (SportsGame sp : games) {
            if (sp.getCategory().getId().equals(con.get().getId()))
                return false;
        }
        this.countryRepository.deleteById(id);
        return true;
    }
}
