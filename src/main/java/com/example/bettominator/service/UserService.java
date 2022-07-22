package com.example.bettominator.service;

import com.example.bettominator.model.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String name, String surname, String email, String phone);

    User findById(String userId);
}
