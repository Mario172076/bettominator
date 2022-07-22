package com.example.bettominator.repostiroy;


import com.example.bettominator.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    void deleteByName(String name);

    List<Country> findCountryByNameLikeIgnoreCase(String name);

    Country findCountryByName(String name);
}
