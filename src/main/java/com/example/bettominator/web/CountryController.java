package com.example.bettominator.web;

import com.example.bettominator.model.Country;
import com.example.bettominator.model.exceptions.CategoryNotFoundException;
import com.example.bettominator.service.CountryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/Countries")
public class CountryController {


    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCountryPage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String conName,
                                 Model model) {
        List<Country> countries;
        if (conName == null) {
            countries = countryService.findAll();
        } else {
            countries = this.countryService.findCountryByNameLike(conName);
        }
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("Countries", countries);
        return "Countries";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteCountry(@PathVariable Long id) {
        if (!this.countryService.deleteCountryById(id)) {
            return "redirect:/Countries?error=CountryIsUsedException";
        } else return "redirect:/Countries?message=CountryDeleteSuccessfully";

    }


    @GetMapping("/edit-form/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCountryPage(@PathVariable Long id, Model model) {
        if (this.countryService.findById(id).isPresent()) {
            Country country = this.countryService.findById(id).get();
            model.addAttribute("country", country);
            return "Add-Country";

        }
        return "redirect:/Countries?error=CountryNotFound";
    }

    @GetMapping("/add-form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCountryPage(Model model) {
        return "Add-Country";
    }


    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String saveCountry(
            @Valid Country country,
            BindingResult bindingResult,
            @RequestParam MultipartFile image,
            Model model
    ) {
        if (country.getId() != null) {
            try {
                this.countryService.edit(country.getId(), country.getName(), image);
            } catch (IOException | CategoryNotFoundException e) {
                return "redirect:/Countries?error=FileSizeTooBig";
            }
        } else {
            try {
                this.countryService.save(country, image);
            } catch (IOException | CategoryNotFoundException e) {
                return "redirect:/Countries?error=" + e.getLocalizedMessage();
            }
        }
        return "redirect:/Countries?message=Success";
    }


}
