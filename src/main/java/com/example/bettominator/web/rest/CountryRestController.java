package com.example.bettominator.web.rest;

import com.example.bettominator.model.Country;
import com.example.bettominator.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/Countries")
public class CountryRestController {

    private final CountryService countryService;


    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> findAll() {
        return this.countryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable Long id) {
        return this.countryService.findById(id)
                .map(country -> ResponseEntity.ok().body(country))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Country> save(@Valid Country country,
                                        BindingResult bindingResult,
                                        @RequestParam MultipartFile image,
                                        Model model) throws IOException {
        return this.countryService.save(country, image)
                .map(cnt -> ResponseEntity.ok().body(cnt))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Country> edit(@PathVariable Long id, String name, MultipartFile image) throws IOException {
        return this.countryService.edit(id, name, image)
                .map(cnt -> ResponseEntity.ok().body(cnt))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.countryService.deleteById(id);
        if (this.countryService.findById(id).isEmpty()) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}