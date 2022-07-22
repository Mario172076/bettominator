package com.example.bettominator.service;

import com.example.bettominator.model.User;

public interface AuthService {
    User login(String username, String password);

    User getCurrentUser();

    String getCurrentUserId();
}
