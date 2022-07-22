package com.example.bettominator.service;

import com.example.bettominator.model.Country;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CountryService {
    Optional<Country> findById(Long id);

    Country findByName(String name);

    List<Country> findCountryByNameLike(String name);

    List<Country> findAll();

    Country create(String name);

    Optional<Country> edit(Long id, String name, MultipartFile image) throws IOException;

    Optional<Country> save(Country country, MultipartFile image) throws IOException;

    void delete(String name);

    void deleteById(Long id);

    boolean deleteCountryById(Long id);
}
