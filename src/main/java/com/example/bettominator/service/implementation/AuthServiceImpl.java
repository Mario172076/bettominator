package com.example.bettominator.service.implementation;

import com.example.bettominator.model.User;
import com.example.bettominator.model.exceptions.LoginCredentialsException;
import com.example.bettominator.repostiroy.UserRepository;
import com.example.bettominator.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new LoginCredentialsException();
        }
        return this.userRepository.findByUsernameAndPassword(username, password).orElseThrow(LoginCredentialsException::new);
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }
}
