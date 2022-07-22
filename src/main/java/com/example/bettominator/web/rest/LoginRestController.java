package com.example.bettominator.web.rest;

import com.example.bettominator.model.User;
import com.example.bettominator.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Login")
public class LoginRestController {

    private final AuthService authService;


    public LoginRestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public User login(String username, String password) {
        return this.authService.login(username, password);
    }
}

