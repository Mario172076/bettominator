package com.example.bettominator.web;

import com.example.bettominator.model.exceptions.*;
import com.example.bettominator.service.AuthService;
import com.example.bettominator.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Register")
public class RegisterController {

    private final AuthService authService;
    private final UserService userService;

    public RegisterController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "Register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String email,
                           @RequestParam String phone) {
        try {
            this.userService.register(username, password, repeatedPassword, name, surname, email, phone);
            return "redirect:/Login";
        } catch (EmptyFieldException | PasswordsDoNotMatchException | PasswordRequirementsException | UserAlreadyExistsException exception) {
            return "redirect:/Register?error=" + exception.getLocalizedMessage();
        }
    }


}
