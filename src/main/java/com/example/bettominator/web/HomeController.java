package com.example.bettominator.web;

import com.example.bettominator.service.FillDataBaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(value = {"/", "/Home"})
public class HomeController {

    private final FillDataBaseService fillDataBaseService;

    public HomeController(FillDataBaseService fillDataBaseService) {
        this.fillDataBaseService = fillDataBaseService;
    }

    @GetMapping
    public String getHomePage(Model model) throws IOException {
 //       this.fillDataBaseService.FillDataBase();
        return "Home";
    }


}