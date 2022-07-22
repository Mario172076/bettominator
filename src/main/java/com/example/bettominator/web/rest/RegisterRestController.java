//package com.example.bettominator.web.rest;
//
//import com.example.bettominator.model.exceptions.LoginCredentialsException;
//import com.example.bettominator.model.exceptions.PasswordsDoNotMatchException;
//import com.example.bettominator.service.UserService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/Register")
//public class RegisterRestController {
//
//    private final UserService userService;
//
//
//    public RegisterRestController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping
//    public String register(@RequestParam String username,
//                           @RequestParam String password,
//                           @RequestParam String repeatedPassword,
//                           @RequestParam String name,
//                           @RequestParam String surname,
//                           @RequestParam String email,
//                           @RequestParam String phone) {
//        try {
//            this.userService.register(username, password, repeatedPassword, name, surname, email, phone);
//            return "redirect:/Login";
//        } catch (LoginCredentialsException | PasswordsDoNotMatchException exception) {
//            return "redirect:/Register?error=" + exception.getMessage();
//        }
//    }
//}
//
